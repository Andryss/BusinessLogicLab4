package ru.andryss.rutube.exception;

public class VideoNotFoundException extends RuntimeException {
    public VideoNotFoundException(String sourceId) {
        super(String.format("video %s not found", sourceId));
    }
}
