package ru.andryss.rutube.interactor;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.variable.value.FileValue;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import ru.andryss.rutube.exception.IllegalVideoFormatException;
import ru.andryss.rutube.exception.NoPublishedVideosException;
import ru.andryss.rutube.message.PutVideoRequest;
import ru.andryss.rutube.message.VideoThumbInfo;
import ru.andryss.rutube.model.Video;
import ru.andryss.rutube.service.ProcessService;
import ru.andryss.rutube.service.SourceService;
import ru.andryss.rutube.service.VideoService;
import ru.andryss.rutube.service.VideoService.VideoChangeInfo;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class VideoInteractorImpl implements VideoInteractor {

    private final SourceService sourceService;
    private final VideoService videoService;
    private final ProcessService processService;

    @Override
    public String handleVideoUpload(String user, FileValue file) {
        String sourceId = UUID.randomUUID().toString();
        String mime = file.getMimeType();
        byte[] content = ((ByteArrayInputStream) file.getValue()).readAllBytes();

        if (!mime.equals("video/mp4") || content.length == 0 || content.length > 2 * 1024 * 1024) {
            throw new IllegalVideoFormatException();
        }

        videoService.createNewVideo(sourceId, user, null);

        sourceService.putVideo(sourceId, file.getFilename(), mime, content);

        return sourceId;
    }

    @Override
    public void handlePutVideoData(String sourceId, PutVideoRequest request, String user) {
        VideoChangeInfo videoChangeInfo = new VideoChangeInfo(request.getTitle().trim(), request.getDescription().trim(),
                request.getCategory(), request.getAccess(), request.isAgeRestriction(), request.isComments());
        if (videoService.putVideo(sourceId, user, videoChangeInfo)) {
            processService.startVideoPublicationProcess(videoService.findVideoById(sourceId), sourceService.getVideo(sourceId));
        }
    }

    @Override
    public void handlePublishVideo(String sourceId) {
        Video video = videoService.publishVideo(sourceId);

        processService.startWatchVideoProcess(video, sourceService.getVideo(sourceId));
    }

    @Override
    public List<VideoThumbInfo> handleFetchPublishedVideos() {
        List<VideoThumbInfo> videoThumbs = videoService.getPublishedVideos(PageRequest.of(0, 200)); // чё?
        if (videoThumbs.size() == 0) {
            throw new NoPublishedVideosException();
        }
        return videoThumbs;
    }
}
