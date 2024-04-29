package ru.andryss.rutube.listener;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Service;
import ru.andryss.rutube.service.VideoService;

@Service
@RequiredArgsConstructor
public class AssignToAuthorTaskListener implements TaskListener {

    private final VideoService service;

    @Override
    public void notify(DelegateTask delegateTask) {
        String sourceId = (String) delegateTask.getVariable("sourceId");
        String author = service.getAuthor(sourceId);
        delegateTask.setAssignee(author);
    }
}
