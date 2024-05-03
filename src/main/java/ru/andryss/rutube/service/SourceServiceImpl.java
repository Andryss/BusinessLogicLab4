package ru.andryss.rutube.service;

import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.andryss.rutube.exception.IllegalVideoFormatException;
import ru.andryss.rutube.exception.IncorrectVideoStatusException;
import ru.andryss.rutube.exception.SourceNotFoundException;
import ru.andryss.rutube.exception.VideoNotFoundException;
import ru.andryss.rutube.model.Source;
import ru.andryss.rutube.model.Video;
import ru.andryss.rutube.repository.SourceRepository;
import ru.andryss.rutube.repository.VideoRepository;

import java.sql.SQLException;

import static ru.andryss.rutube.model.VideoStatus.MODERATION_PENDING;
import static ru.andryss.rutube.model.VideoStatus.UPLOAD_PENDING;

@Service
@RequiredArgsConstructor
public class SourceServiceImpl implements SourceService {

    private final SourceRepository sourceRepository;
    private final VideoRepository videoRepository;
    private final ModerationRequestSenderService requestSenderService;
    private final TransactionTemplate transactionTemplate;
    private final TransactionTemplate readOnlyTransactionTemplate;

    @Override
    @Retryable(retryFor = SQLException.class)
    public void putVideo(String sourceId, String filename, String mime, byte[] content) {
        transactionTemplate.executeWithoutResult(status -> {
            Video video = videoRepository.findById(sourceId).orElseThrow(() -> new VideoNotFoundException(sourceId));
            if (video.getStatus() != UPLOAD_PENDING) {
                throw new IncorrectVideoStatusException(video.getStatus(), UPLOAD_PENDING);
            }

            video.setStatus(MODERATION_PENDING);

            Source source = new Source();
            source.setSourceId(sourceId);
            source.setFilename(filename);
            source.setMime(mime);
            source.setContent(content);

            sourceRepository.save(source);
            videoRepository.save(video);
        });

        requestSenderService.sendFirstTime(sourceId, generateDownloadLink(sourceId));
    }

    @Override
    public String generateDownloadLink(String sourceId) {
        return String.format("/api/source/%s", sourceId);
    }

    @Override
    public Source getVideo(String sourceId) {
        return readOnlyTransactionTemplate.execute(status ->
            sourceRepository.findById(sourceId).orElseThrow(() -> new SourceNotFoundException(sourceId))
        );
    }
}
