package ru.andryss.rutube.listener;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;
import ru.andryss.rutube.interactor.VideoInteractor;

@Service
@RequiredArgsConstructor
public class VideoPublisher implements JavaDelegate {

    private final VideoInteractor interactor;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String sourceId = (String) execution.getVariable("sourceId");

        interactor.handlePublishVideo(sourceId);
    }
}
