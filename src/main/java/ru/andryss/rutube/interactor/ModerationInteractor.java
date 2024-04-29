package ru.andryss.rutube.interactor;

import ru.andryss.rutube.message.ModerationRequestInfo;
import ru.andryss.rutube.message.ModerationResendInfo;
import ru.andryss.rutube.message.ModerationResultInfo;
import ru.andryss.rutube.message.UploadModerationResultRequest;

/**
 * Interactor for handling moderation requests
 */
public interface ModerationInteractor {
    /**
     * Handles message from moderation.requests topic
     */
    void moderationRequestMessage(ModerationRequestInfo request);
    /**
     * Handles message from moderation.results topic
     */
    void moderationResultMessage(ModerationResultInfo result);
    /**
     * Handles message from moderation.resend topic
     */
    void moderationResendMessage(ModerationResendInfo resend);
    /**
     * Handles success moderation event
     */
    void handleSuccessModeration(UploadModerationResultRequest request, String user);
    /**
     * Handles failure moderation event
     */
    void handleFailureModeration(UploadModerationResultRequest request, String user);
    /**
     * Handles moderation assignee update
     */
    void handleAssigneeUpdate(String sourceId, String assignee);
}
