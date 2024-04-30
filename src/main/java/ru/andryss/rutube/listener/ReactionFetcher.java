package ru.andryss.rutube.listener;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;
import ru.andryss.rutube.interactor.ReactionInteractor;
import ru.andryss.rutube.interactor.ReactionInteractor.ReactionsInfo;
import ru.andryss.rutube.message.CommentInfo;
import ru.andryss.rutube.message.ReactionInfo;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReactionFetcher implements JavaDelegate {

    private final ReactionInteractor interactor;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String sourceId = (String) execution.getVariable("sourceId");

        ReactionsInfo reactionsInfo = interactor.handleReactionsFetch(sourceId);

        execution.setVariable("time", reactionsInfo.timestamp().toString());
        execution.setVariable("reactions", formatReactions(reactionsInfo.reactions()));
        execution.setVariable("comments", formatComments(reactionsInfo.comments()));
    }

    private String formatComments(List<CommentInfo> comments) {
        StringBuilder builder = new StringBuilder();
        comments.forEach(info -> builder
                .append(info.getPostedAt()).append(" - ")
                .append(info.getAuthor()).append(" - ")
                .append(info.getContent()).append('\n')
        );
        return builder.toString();
    }

    private String formatReactions(List<ReactionInfo> reactions) {
        StringBuilder builder = new StringBuilder();
        reactions.forEach(info -> builder
                .append(info.getReaction()).append(" - ")
                .append(info.getCount()).append('\n')
        );
        return builder.toString();
    }
}
