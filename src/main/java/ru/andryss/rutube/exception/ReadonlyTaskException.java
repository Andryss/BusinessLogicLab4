package ru.andryss.rutube.exception;

public class ReadonlyTaskException extends RuntimeException {
    public ReadonlyTaskException() {
        super("task is read only and cant be completed");
    }
}
