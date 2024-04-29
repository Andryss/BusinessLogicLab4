package ru.andryss.rutube.service;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProcessServiceImpl implements ProcessService {

    private final RuntimeService runtimeService;

    @Override
    public void startModerationProcess(String sourceId, String downloadLink) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("sourceId", sourceId);
        variables.put("downloadLink", downloadLink);

        runtimeService.startProcessInstanceByKey("Process_moderate_video", variables);
    }

    @Override
    public void startModerationRejectProcess(String sourceId, String downloadLink, String comment) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("sourceId", sourceId);
        variables.put("downloadLink", downloadLink);
        variables.put("comment", comment);

        runtimeService.startProcessInstanceByKey("Process_moderation_reject", variables);
    }

    @Override
    public void startVideoPublicationProcess(String sourceId, String downloadLink) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("sourceId", sourceId);
        variables.put("downloadLink", downloadLink);

        runtimeService.startProcessInstanceByKey("Process_publish_video", variables);
    }
}
