package ru.andryss.rutube.interactor;

import ru.andryss.rutube.message.CommentInfo;
import ru.andryss.rutube.message.ReactionInfo;
import ru.andryss.rutube.model.ReactionType;

import java.time.Instant;
import java.util.List;

/**
 * Interactor for handling reaction requests
 */
public interface ReactionInteractor {
    /**
     * Handles leave reaction event
     */
    void handleLeaveReaction(String sourceId, String user, ReactionType reaction, String comment);
    /**
     * Handles reactions fetch event
     */
    ReactionsInfo handleReactionsFetch(String sourceId);

    record ReactionsInfo(Instant timestamp, List<ReactionInfo> reactions, List<CommentInfo> comments) { }
}
