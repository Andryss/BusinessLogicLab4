package ru.andryss.rutube.listener;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;
import ru.andryss.rutube.interactor.VideoInteractor;
import ru.andryss.rutube.message.VideoThumbInfo;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublishedVideoFetcher implements JavaDelegate {

    private final VideoInteractor interactor;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        List<VideoThumbInfo> thumbs = interactor.handleFetchPublishedVideos();
        execution.setVariable("availableVideos", formatAvailableVideos(thumbs));
    }

    private String formatAvailableVideos(List<VideoThumbInfo> thumbs) {
        StringBuilder builder = new StringBuilder();
        thumbs.forEach(info -> builder
                .append(info.getTitle()).append(" - ")
                .append(info.getAuthor()).append(" - ")
                .append(info.getVideoId()).append('\n')
        );
        return builder.toString();
    }
}
