package ru.andryss.rutube.service;

import ru.andryss.rutube.model.ModerationStatus;

import java.time.Instant;

/**
 * Service for working with moderation
 */
public interface ModerationService {
    /**
     * Handle new moderation request
     *
     * @param sourceId source to moderate
     * @param downloadLink link to view source
     * @param createdAt request creation time
     */
    void handleRequest(String sourceId, String downloadLink, Instant createdAt);

    /**
     * Handle new moderation result
     *
     * @param sourceId moderated source
     * @param status moderation result
     * @param comment comment
     * @param createdAt creation timestamp
     * @return true if video is ready to publish now
     */
    boolean handleResult(String sourceId, ModerationStatus status, String comment, Instant createdAt);

    /**
     * Sends message to request moderation resending
     *
     * @param sourceId source to request
     */
    void requestResend(String sourceId);

    /**
     * Handle moderation resend
     *
     * @param sourceId source to resend
     */
    void handleResend(String sourceId);

    /**
     * Saves moderation result
     *
     * @param sourceId moderated source
     * @param username moderator
     * @param status moderation result
     * @param comment result comment (e.g. reject reason)
     */
    void uploadModeration(String sourceId, String username, ModerationStatus status, String comment);

    /**
     * Assigns moderation request to user
     *
     * @param sourceId source to assign
     * @param assignee user
     */
    void assignRequest(String sourceId, String assignee);
}
