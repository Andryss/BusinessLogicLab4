package ru.andryss.rutube.service;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.stereotype.Service;
import ru.andryss.rutube.model.Source;
import ru.andryss.rutube.model.Video;

import java.util.HashMap;
import java.util.Map;

import static ru.andryss.rutube.service.ProcessService.buildFile;

@Service
@RequiredArgsConstructor
public class ProcessServiceImpl implements ProcessService {

    private final RuntimeService runtimeService;

    @Override
    public void startModerationProcess(String sourceId, Source file) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("sourceId", sourceId);
        variables.put("file", buildFile(file));

        runtimeService.startProcessInstanceByKey("Process_moderate_video", variables);
    }

    @Override
    public void startModerationRejectProcess(String sourceId, Source file, String comment) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("sourceId", sourceId);
        variables.put("file", buildFile(file));
        variables.put("comment", comment);

        runtimeService.startProcessInstanceByKey("Process_moderation_reject", variables);
    }

    @Override
    public void startVideoPublicationProcess(Video video, Source file) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("sourceId", video.getSourceId());
        variables.put("file", buildFile(file));
        variables.put("title", video.getTitle());
        variables.put("description", video.getDescription());
        variables.put("category", video.getCategory().toString());
        variables.put("ageRestriction", video.isAgeRestriction());
        variables.put("comments", video.isComments());

        runtimeService.startProcessInstanceByKey("Process_publish_video", variables);
    }

    @Override
    public void startWatchVideoProcess(Video video, Source file) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("file", buildFile(file));
        variables.put("title", video.getTitle());
        variables.put("description", video.getDescription());
        variables.put("category", video.getCategory().toString());
        variables.put("ageRestriction", video.isAgeRestriction());
        variables.put("comments", video.isComments());

        runtimeService.startProcessInstanceByKey("Process_watch_video", variables);
    }
}
