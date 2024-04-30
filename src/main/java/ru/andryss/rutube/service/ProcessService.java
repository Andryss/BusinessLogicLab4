package ru.andryss.rutube.service;

import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.FileValue;
import ru.andryss.rutube.model.Source;
import ru.andryss.rutube.model.Video;

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
     * @param video source to publish
     * @param file source raw file
     */
    void startVideoPublicationProcess(Video video, Source file);
    /**
     * Starts video watching process
     *
     * @param video video to watch
     * @param file source raw file
     */
    void startWatchVideoProcess(Video video, Source file);

    static FileValue buildFile(Source file) {
        return Variables
                .fileValue(file.getFilename())
                .file(file.getContent())
                .mimeType(file.getMime())
                .create();
    }
}
