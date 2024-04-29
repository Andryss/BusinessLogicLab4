package ru.andryss.rutube.interactor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.andryss.rutube.message.PutVideoRequest;
import ru.andryss.rutube.service.ProcessService;
import ru.andryss.rutube.service.SourceService;
import ru.andryss.rutube.service.VideoService;
import ru.andryss.rutube.service.VideoService.VideoChangeInfo;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class VideoInteractorImpl implements VideoInteractor {

    private final SourceService sourceService;
    private final VideoService videoService;
    private final ProcessService processService;

    @Override
    public String handleVideoUpload(String user, byte[] file) {
        String sourceId = UUID.randomUUID().toString();

        videoService.createNewVideo(sourceId, user, null);

        sourceService.putVideo(sourceId, file);

        return sourceId;
    }

    @Override
    public void handlePutVideoData(String sourceId, PutVideoRequest request, String user) {
        VideoChangeInfo videoChangeInfo = new VideoChangeInfo(request.getTitle().trim(), request.getDescription().trim(),
                request.getCategory(), request.getAccess(), request.isAgeRestriction(), request.isComments());
        if (videoService.putVideo(sourceId, user, videoChangeInfo)) {
            processService.startVideoPublicationProcess(sourceId, sourceService.generateDownloadLink(sourceId));
        }
    }

    @Override
    public void handlePublishVideo(String sourceId) {
        videoService.publishVideo(sourceId);
    }
}
