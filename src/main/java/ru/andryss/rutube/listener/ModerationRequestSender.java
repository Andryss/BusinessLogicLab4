package ru.andryss.rutube.listener;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.value.FileValue;
import org.springframework.stereotype.Service;
import ru.andryss.rutube.interactor.VideoInteractor;

@Service
@RequiredArgsConstructor
public class ModerationRequestSender implements JavaDelegate {

    private final VideoInteractor interactor;
    private final RuntimeService runtimeService;

    @Override
    public void execute(DelegateExecution execution) {
        String user = (String) execution.getVariable("initiator");
        FileValue file = runtimeService.getVariableTyped(execution.getId(), "file");

        String sourceId = interactor.handleVideoUpload(user, file);

        execution.setVariable("sourceId", sourceId);
    }
}
