package ru.andryss.rutube.listener;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;
import ru.andryss.rutube.interactor.ReactionInteractor;
import ru.andryss.rutube.model.ReactionType;

@Service
@RequiredArgsConstructor
public class ReactionSaver implements JavaDelegate {

    private final ReactionInteractor interactor;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String user = (String) execution.getVariable("initiator");
        String sourceId = (String) execution.getVariable("sourceId");
        String reaction = (String) execution.getVariable("reaction");
        String comment = (String) execution.getVariable("comment");

        ReactionType reactionType = (reaction.equals("NONE") ? null : ReactionType.valueOf(reaction));

        interactor.handleLeaveReaction(sourceId, user, reactionType, comment);
    }
}
