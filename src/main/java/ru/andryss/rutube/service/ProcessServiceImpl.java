package ru.andryss.rutube.service;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.FileValue;
import org.springframework.stereotype.Service;
import ru.andryss.rutube.model.Source;

import java.util.HashMap;
import java.util.Map;

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
    public void startVideoPublicationProcess(String sourceId, Source file) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("sourceId", sourceId);
        variables.put("file", buildFile(file));

        runtimeService.startProcessInstanceByKey("Process_publish_video", variables);
    }

    private FileValue buildFile(Source file) {
        return Variables
                .fileValue(file.getFilename())
                .file(file.getContent())
                .mimeType(file.getMime())
                .create();
    }
}
