package ru.andryss.rutube.exception;

public class SourceNotFoundException extends RuntimeException {
    public SourceNotFoundException(String sourceId) {
        super(String.format("source %s not found", sourceId));
    }
}
