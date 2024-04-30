package ru.andryss.rutube.exception;

import lombok.Getter;
import ru.andryss.rutube.model.VideoStatus;

@Getter
public class IncorrectVideoStatusException extends RuntimeException {
    public IncorrectVideoStatusException(VideoStatus real, VideoStatus expected) {
        super(String.format("video is in %s state, but %s expected", real, expected));
    }
}
