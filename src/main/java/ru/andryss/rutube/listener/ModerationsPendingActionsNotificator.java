package ru.andryss.rutube.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;
import ru.andryss.rutube.service.MailService;
import ru.andryss.rutube.service.ModerationService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ModerationsPendingActionsNotificator implements JavaDelegate {

    private final ModerationService moderationService;
    private final MailService mailService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Start sending moderation pending action notifications");

        Instant timestamp = Instant.now().minus(1L, ChronoUnit.HOURS);

        moderationService.findRequestsAssignedBefore(timestamp).forEach(info ->
                mailService.sendModerationPendingNotification(info.getAssignee(), info.getEmail(), info.getSourceId()));

        log.info("Sending moderation pending action notifications finished");
    }
}
