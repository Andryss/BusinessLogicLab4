package ru.andryss.rutube.exception;

public class NoPublishedVideosException extends RuntimeException {
    public NoPublishedVideosException() {
        super("no published videos yet");
    }
}
