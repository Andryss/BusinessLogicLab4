package ru.andryss.rutube.listener;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;
import ru.andryss.rutube.interactor.VideoInteractor;
import ru.andryss.rutube.message.PutVideoRequest;
import ru.andryss.rutube.model.VideoAccess;
import ru.andryss.rutube.model.VideoCategory;

import java.io.ByteArrayInputStream;

@Service
@RequiredArgsConstructor
public class VideoDataSaver implements JavaDelegate {

    private final VideoInteractor interactor;

    @Override
    public void execute(DelegateExecution execution) {
        String sourceId = (String) execution.getVariable("sourceId");
        String user = (String) execution.getVariable("initiator");

        PutVideoRequest request = new PutVideoRequest();
        request.setTitle((String) execution.getVariable("title"));
        request.setDescription((String) execution.getVariable("description"));
        request.setCategory(VideoCategory.valueOf((String) execution.getVariable("category")));
        request.setAccess(VideoAccess.valueOf((String) execution.getVariable("access")));
        request.setAgeRestriction((Boolean) execution.getVariable("ageRestriction"));
        request.setComments((Boolean) execution.getVariable("comments"));

        interactor.handlePutVideoData(sourceId, request, user);
    }
}
