package ru.andryss.rutube.interactor;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import ru.andryss.rutube.message.CommentInfo;
import ru.andryss.rutube.message.ReactionInfo;
import ru.andryss.rutube.model.ReactionType;
import ru.andryss.rutube.service.CommentService;
import ru.andryss.rutube.service.ReactionService;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReactionInteractorImpl implements ReactionInteractor {

    private final ReactionService reactionService;
    private final CommentService commentService;

    @Override
    public void handleLeaveReaction(String sourceId, String user, ReactionType reaction, String comment) {
        if (reaction != null) {
            reactionService.createReaction(sourceId, user, reaction);
        }
        if (comment != null) {
            comment = comment.trim();
            if (!comment.equals("")) {
                commentService.createComment(sourceId, user, null, comment);
            }
        }
    }

    @Override
    public ReactionsInfo handleReactionsFetch(String sourceId) {
        List<ReactionInfo> allReactions = reactionService.getAllReactions(sourceId);
        List<CommentInfo> comments = commentService.getComments(sourceId, null, PageRequest.of(0, 200)); // чё?
        return new ReactionsInfo(Instant.now(), allReactions, comments);
    }
}
