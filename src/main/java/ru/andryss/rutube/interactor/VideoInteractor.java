package ru.andryss.rutube.interactor;

import org.camunda.bpm.engine.variable.value.FileValue;
import ru.andryss.rutube.message.PutVideoRequest;

/**
 * Interactor for handling video requests
 */
public interface VideoInteractor {
    /**
     * Handles video upload event
     */
    String handleVideoUpload(String user, FileValue file);
    /**
     * Handles put video data event
     */
    void handlePutVideoData(String sourceId, PutVideoRequest request, String user);
    /**
     * Handles video publish event
     */
    void handlePublishVideo(String sourceId);
}
