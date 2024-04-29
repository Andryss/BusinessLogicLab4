package ru.andryss.rutube.listener;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;
import ru.andryss.rutube.interactor.VideoInteractor;

import java.io.ByteArrayInputStream;

@Service
@RequiredArgsConstructor
public class ModerationRequestSender implements JavaDelegate {

    private final VideoInteractor interactor;

    @Override
    public void execute(DelegateExecution execution) {
        String user = (String) execution.getVariable("initiator");
        byte[] file = ((ByteArrayInputStream) execution.getVariable("file")).readAllBytes();

        String sourceId = interactor.handleVideoUpload(user, file);

        execution.setVariable("sourceId", sourceId);
    }
}
