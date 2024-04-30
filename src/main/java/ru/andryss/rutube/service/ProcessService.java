package ru.andryss.rutube.service;

import ru.andryss.rutube.model.Source;

/**
 * Service for working with camunda bpm process
 */
public interface ProcessService {
    /**
     * Starts source moderation process
     *
     * @param sourceId source to moderate
     * @param file source raw file
     */
    void startModerationProcess(String sourceId, Source file);
    /**
     * Starts moderation reject process
     *
     * @param sourceId source to moderate
     * @param file source raw file
     */
    void startModerationRejectProcess(String sourceId, Source file, String comment);
    /**
     * Starts video publication process
     *
     * @param sourceId source to publish
     * @param file source raw file
     */
    void startVideoPublicationProcess(String sourceId, Source file);
}
