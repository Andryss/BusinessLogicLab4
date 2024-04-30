package ru.andryss.rutube.exception;

public class VideoAlreadyPublishedException extends RuntimeException {
    public VideoAlreadyPublishedException(String sourceId) {
        super(String.format("video %s already published", sourceId));
    }
}
