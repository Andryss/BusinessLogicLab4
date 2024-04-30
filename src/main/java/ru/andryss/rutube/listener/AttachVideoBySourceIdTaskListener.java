package ru.andryss.rutube.listener;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Service;
import ru.andryss.rutube.service.SourceService;
import ru.andryss.rutube.service.VideoService;

import static ru.andryss.rutube.service.ProcessService.buildFile;

@Service
@RequiredArgsConstructor
public class AttachVideoBySourceIdTaskListener implements TaskListener {

    private final SourceService sourceService;
    private final VideoService videoService;

    @Override
    public void notify(DelegateTask delegateTask) {
        String sourceId = (String) delegateTask.getVariable("sourceId");
        delegateTask.setVariable("file", buildFile(sourceService.getVideo(sourceId)));
        delegateTask.setVariable("commentsAvailable", videoService.findVideoById(sourceId).isComments());
    }
}
