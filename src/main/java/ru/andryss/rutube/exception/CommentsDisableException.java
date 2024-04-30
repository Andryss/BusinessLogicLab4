package ru.andryss.rutube.exception;

public class CommentsDisableException extends RuntimeException {
    public CommentsDisableException(String sourceId) {
        super(String.format("comments disabled for source %s", sourceId));
    }
}
