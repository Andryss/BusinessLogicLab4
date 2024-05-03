package ru.andryss.rutube.listener;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Service;
import ru.andryss.rutube.exception.ReadonlyTaskException;

@Service
public class NeverCompleteTaskListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        throw new ReadonlyTaskException();
    }
}
