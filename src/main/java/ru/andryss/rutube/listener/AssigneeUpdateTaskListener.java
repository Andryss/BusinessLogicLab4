package ru.andryss.rutube.listener;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Service;
import ru.andryss.rutube.interactor.ModerationInteractor;

@Service
@RequiredArgsConstructor
public class AssigneeUpdateTaskListener implements TaskListener {

    private final ModerationInteractor interactor;

    @Override
    public void notify(DelegateTask delegateTask) {
        String assignee = delegateTask.getAssignee();
        String sourceId = (String) delegateTask.getVariable("sourceId");
        interactor.handleAssigneeUpdate(sourceId, assignee);
    }
}
