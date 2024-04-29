package ru.andryss.rutube.service;

import org.springframework.data.domain.PageRequest;
import ru.andryss.rutube.message.VideoThumbInfo;
import ru.andryss.rutube.model.*;

import java.time.Instant;
import java.util.List;

/**
 * Service for working with videos
 */
public interface VideoService {
    /**
     * Creates new video in draft state
     *
     * @param sourceId uploaded video identifier
     * @param author uploading user
     * @param prototype video to copy its info into new video
     */
    void createNewVideo(String sourceId, String author, String prototype);

    /**
     * Puts video info by given identifier
     *
     * @param sourceId video to put info
     * @param author putting user
     * @param videoChangeInfo video info to put
     * @return true if video ready to publish
     */
    boolean putVideo(String sourceId, String author, VideoChangeInfo videoChangeInfo);

    /**
     * Publishes given video
     *
     * @param sourceId video to publish
     */
    void publishVideo(String sourceId);

    /**
     * Finds published videos
     *
     * @return published videos
     */
    List<VideoThumbInfo> getPublishedVideos(PageRequest pageRequest);

    /**
     * Finds published video
     *
     * @param sourceId video to search
     * @return found video
     */
    Video findPublishedVideo(String sourceId);

    /**
     * Finds all users with videos pending user actions (UPLOAD_PENDING, FILL_PENDING) more than specified time
     *
     * @param timestamp videos updated earlier than timestamp will be ignored
     * @return found users
     */
    List<User> findUsersWithPendingActions(Instant timestamp);

    /**
     * Finds user videos pending his actions (UPLOAD_PENDING, FILL_PENDING) more than specified time
     *
     * @param author user to search
     * @param timestamp videos updated earlier than timestamp will be ignored
     * @return found videos
     */
    List<Video> findVideosPendingActions(String author, Instant timestamp);

    /**
     * Finds videos pending moderations more then specified time
     *
     * @param timestamp videos updated earlier than timestamp will be ignored
     * @return found source ids
     */
    List<String> findVideosPendingModeration(Instant timestamp);

    /**
     * Returns author of given source
     *
     * @param sourceId video to search
     * @return author
     */
    String getAuthor(String sourceId);

    record VideoChangeInfo(String title, String description, VideoCategory category, VideoAccess access,
                           Boolean ageRestriction, Boolean comments) { }
}
