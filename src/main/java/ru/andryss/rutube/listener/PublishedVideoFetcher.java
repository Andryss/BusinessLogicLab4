package ru.andryss.rutube.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;
import ru.andryss.rutube.interactor.VideoInteractor;
import ru.andryss.rutube.message.VideoThumbInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PublishedVideoFetcher implements JavaDelegate {

    private final VideoInteractor interactor;
    private final ObjectMapper mapper;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        List<VideoThumbInfo> thumbs = interactor.handleFetchPublishedVideos();
        execution.setVariable("availableVideos", formatAvailableVideos(thumbs));
    }

    @SneakyThrows
    private String formatAvailableVideos(List<VideoThumbInfo> thumbs) {
        Map<String, String> sourceIdToTitle = new HashMap<>();
        thumbs.forEach(thumb -> sourceIdToTitle.put(thumb.getVideoId(), thumb.getTitle()));
        return mapper.writeValueAsString(sourceIdToTitle);
    }
}
