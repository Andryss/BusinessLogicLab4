package ru.andryss.rutube.listener;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;
import ru.andryss.rutube.interactor.ModerationInteractor;
import ru.andryss.rutube.message.UploadModerationResultRequest;
import ru.andryss.rutube.model.ModerationStatus;

@Service
@RequiredArgsConstructor
public class PublicationApprover implements JavaDelegate {

    private final ModerationInteractor interactor;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String user = (String) execution.getVariable("assignee");

        UploadModerationResultRequest request = new UploadModerationResultRequest();
        request.setSourceId((String) execution.getVariable("sourceId"));
        request.setResult(ModerationStatus.SUCCESS);
        request.setComment((String) execution.getVariable("comment"));

        interactor.handleSuccessModeration(request, user);
    }
}
