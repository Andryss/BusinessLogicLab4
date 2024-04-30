package ru.andryss.rutube.exception;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(String commentId) {
        super(String.format("comment %s not found", commentId));
    }
}
