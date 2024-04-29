package ru.andryss.rutube.service;

/**
 * Service for working with camunda bpm process
 */
public interface ProcessService {
    /**
     * Starts source moderation process
     *
     * @param sourceId source to moderate
     * @param downloadLink link to download source
     */
    void startModerationProcess(String sourceId, String downloadLink);
    /**
     * Starts moderation reject process
     *
     * @param sourceId source to moderate
     * @param downloadLink link to download source
     */
    void startModerationRejectProcess(String sourceId, String downloadLink, String comment);
    /**
     * Starts video publication process
     *
     * @param sourceId source to publish
     * @param downloadLink link to download source
     */
    void startVideoPublicationProcess(String sourceId, String downloadLink);
}
