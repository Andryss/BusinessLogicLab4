package ru.andryss.rutube.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.andryss.rutube.exception.IncorrectVideoStatusException;
import ru.andryss.rutube.exception.SourceNotFoundException;
import ru.andryss.rutube.exception.VideoNotFoundException;
import ru.andryss.rutube.message.ModerationResendInfo;
import ru.andryss.rutube.message.ModerationResultInfo;
import ru.andryss.rutube.model.*;
import ru.andryss.rutube.repository.ModerationHistoryRepository;
import ru.andryss.rutube.repository.ModerationRequestRepository;
import ru.andryss.rutube.repository.ModerationResultRepository;
import ru.andryss.rutube.repository.VideoRepository;

import java.sql.SQLException;
import java.time.Instant;
import java.util.Optional;

import static ru.andryss.rutube.model.ModerationStatus.SUCCESS;
import static ru.andryss.rutube.model.VideoStatus.*;

@Service
@RequiredArgsConstructor
public class ModerationServiceImpl implements ModerationService {

    private final ModerationResultRepository resultRepository;
    private final ModerationRequestRepository requestRepository;
    private final ModerationHistoryRepository historyRepository;
    private final VideoRepository videoRepository;
    private final SourceService sourceService;
    private final ModerationRequestSenderService requestSenderService;
    private final KafkaProducer<String, Object> kafkaProducer;
    private final TransactionTemplate transactionTemplate;

    @Value("${topic.moderation.results}")
    private String moderationResultsTopic;

    @Value("${topic.moderation.resends}")
    private String moderationResendsTopic;

    @Override
    @Retryable(retryFor = SQLException.class)
    public void handleRequest(String sourceId, String downloadLink, Instant createdAt) {
        transactionTemplate.executeWithoutResult(status -> {
            Optional<ModerationHistory> alreadyModerated = historyRepository.findById(sourceId);
            if (alreadyModerated.isPresent()) {
                ModerationHistory history = alreadyModerated.get();

                ModerationResultInfo resultInfo = new ModerationResultInfo();
                resultInfo.setSourceId(sourceId);
                resultInfo.setStatus(history.getStatus());
                resultInfo.setComment(history.getComment());
                resultInfo.setCreatedAt(history.getCreatedAt());

                kafkaProducer.send(new ProducerRecord<>(moderationResultsTopic, resultInfo));
                return;
            }

            if (requestRepository.existsById(sourceId)) {
                return;
            }

            ModerationRequest request = new ModerationRequest();
            request.setSourceId(sourceId);
            request.setDownloadLink(downloadLink);
            request.setCreatedAt(createdAt);

            requestRepository.save(request);
        });
    }

    @Override
    public boolean handleResult(String sourceId, ModerationStatus status, String comment, Instant createdAt) {
        return Boolean.TRUE.equals(transactionTemplate.execute(s -> {
            if (resultRepository.existsById(sourceId)) {
                return false;
            }

            Video video = videoRepository.findById(sourceId).orElseThrow(() -> new VideoNotFoundException(sourceId));
            if (video.getStatus() != MODERATION_PENDING) {
                throw new IncorrectVideoStatusException(video.getStatus(), MODERATION_PENDING);
            }

            ModerationResult result = new ModerationResult();
            result.setSourceId(sourceId);
            result.setStatus(status);
            result.setComment(comment);
            result.setCreatedAt(createdAt);

            boolean isReady = false;
            if (status != SUCCESS) {
                video.setStatus(MODERATION_FAILED);
            } else if (video.getTitle() != null && video.getDescription() != null && video.getCategory() != null && video.getAccess() != null) {
                video.setStatus(READY);
                isReady = true;
            } else {
                video.setStatus(FILL_PENDING);
            }
            video.setUpdatedAt(Instant.now());

            resultRepository.save(result);
            videoRepository.save(video);
            return isReady;
        }));
    }

    @Override
    public void requestResend(String sourceId) {
        ModerationResendInfo resendInfo = new ModerationResendInfo();
        resendInfo.setSourceId(sourceId);

        kafkaProducer.send(new ProducerRecord<>(moderationResendsTopic, resendInfo));
    }

    @Override
    public void handleResend(String sourceId) {
        requestSenderService.resend(sourceId, sourceService.generateDownloadLink(sourceId));
    }

    @Override
    public void uploadModeration(String sourceId, String username, ModerationStatus status, String comment) {
        Instant now = Instant.now();

        transactionTemplate.executeWithoutResult(s -> {
            ModerationRequest request = requestRepository.findBySourceIdAndAssignee(sourceId, username).orElseThrow(() -> new SourceNotFoundException(sourceId));

            if (!request.getSourceId().equals(sourceId)) {
                throw new SourceNotFoundException(sourceId);
            }

            ModerationHistory history = new ModerationHistory();
            history.setSourceId(sourceId);
            history.setAssignee(username);
            history.setStatus(status);
            history.setComment(comment);
            history.setCreatedAt(now);

            historyRepository.save(history);
            requestRepository.delete(request);
        });

        ModerationResultInfo resultInfo = new ModerationResultInfo();
        resultInfo.setSourceId(sourceId);
        resultInfo.setStatus(status);
        resultInfo.setComment(comment);
        resultInfo.setCreatedAt(now);

        kafkaProducer.send(new ProducerRecord<>(moderationResultsTopic, resultInfo));
    }

    @Override
    public void assignRequest(String sourceId, String assignee) {
        transactionTemplate.executeWithoutResult(s ->
                requestRepository.assignModeration(sourceId, assignee, Instant.now())
        );
    }
}
