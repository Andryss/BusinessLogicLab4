package ru.andryss.rutube.exception;

public class ParentSourceDifferentException extends RuntimeException {
    public ParentSourceDifferentException(String parentId) {
        super(String.format("parent comment %s has different source id", parentId));
    }
}
