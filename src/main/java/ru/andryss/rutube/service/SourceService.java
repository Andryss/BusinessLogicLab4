package ru.andryss.rutube.service;

import ru.andryss.rutube.model.Source;

/**
 * Service for working with sources
 */
public interface SourceService {

    /**
     * Uploads video by generated link
     *
     * @param sourceId source id to be uploaded
     * @param content video file
     */
    void putVideo(String sourceId, String filename, String mime, byte[] content);

    /**
     * Generates link for source downloading
     *
     * @param sourceId source id to be downloaded
     * @return download link
     */
    String generateDownloadLink(String sourceId);

    /**
     * Gets video by generated link
     *
     * @param sourceId source id to be downloaded
     * @return video file
     */
    Source getVideo(String sourceId);
}
