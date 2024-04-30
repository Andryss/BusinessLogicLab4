package ru.andryss.rutube.exception;

public class IllegalVideoFormatException extends RuntimeException {
    public IllegalVideoFormatException() {
        super("video has illegal format");
    }
}
