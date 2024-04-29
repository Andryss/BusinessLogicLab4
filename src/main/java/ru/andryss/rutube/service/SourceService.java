package ru.andryss.rutube.service;

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
    void putVideo(String sourceId, byte[] content);

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
    byte[] getVideo(String sourceId);
}
