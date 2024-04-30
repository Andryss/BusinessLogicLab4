package ru.andryss.rutube.interactor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.andryss.rutube.message.ModerationRequestInfo;
import ru.andryss.rutube.message.ModerationResendInfo;
import ru.andryss.rutube.message.ModerationResultInfo;
import ru.andryss.rutube.message.UploadModerationResultRequest;
import ru.andryss.rutube.model.ModerationStatus;
import ru.andryss.rutube.model.Source;
import ru.andryss.rutube.service.ModerationService;
import ru.andryss.rutube.service.ProcessService;
import ru.andryss.rutube.service.SourceService;
import ru.andryss.rutube.service.VideoService;

@Slf4j
@Component
@RequiredArgsConstructor
public class ModerationInteractorImpl implements ModerationInteractor {

    private final ModerationService moderationService;
    private final SourceService sourceService;
    private final ProcessService processService;
    private final VideoService videoService;

    @Override
    public void moderationRequestMessage(ModerationRequestInfo request) {
        log.info("got ModerationRequest message {}", request);
        String sourceId = request.getSourceId();

        moderationService.handleRequest(sourceId, request.getDownloadLink(), request.getCreatedAt());

        processService.startModerationProcess(sourceId, sourceService.getVideo(sourceId));
    }

    @Override
    public void moderationResultMessage(ModerationResultInfo result) {
        log.info("got ModerationResult message {}", result);

        String sourceId = result.getSourceId();
        String comment = result.getComment();

        boolean isReady = moderationService.handleResult(sourceId, result.getStatus(), comment, result.getCreatedAt());

        Source video = sourceService.getVideo(sourceId);

        if (result.getStatus() == ModerationStatus.FAILURE) {
            processService.startModerationRejectProcess(sourceId, video, comment);
        }

        if (isReady) {
            processService.startVideoPublicationProcess(videoService.findVideoById(sourceId), video);
        }
    }

    @Override
    public void moderationResendMessage(ModerationResendInfo resend) {
        log.info("got ModerationResend message {}", resend);
        moderationService.handleResend(resend.getSourceId());
    }

    @Override
    public void handleSuccessModeration(UploadModerationResultRequest request, String user) {
        moderationService.uploadModeration(request.getSourceId(), user, request.getResult(), request.getComment());
    }

    @Override
    public void handleFailureModeration(UploadModerationResultRequest request, String user) {
        moderationService.uploadModeration(request.getSourceId(), user, request.getResult(), request.getComment());
    }

    @Override
    public void handleAssigneeUpdate(String sourceId, String assignee) {
        moderationService.assignRequest(sourceId, assignee);
    }
}
