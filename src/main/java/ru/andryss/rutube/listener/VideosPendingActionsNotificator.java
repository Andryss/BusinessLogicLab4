package ru.andryss.rutube.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;
import ru.andryss.rutube.model.Video;
import ru.andryss.rutube.model.VideoStatus;
import ru.andryss.rutube.service.MailService;
import ru.andryss.rutube.service.VideoService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

@Slf4j
@Service
@RequiredArgsConstructor
public class VideosPendingActionsNotificator implements JavaDelegate {

    private final VideoService videoService;
    private final MailService mailService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Start sending videos pending actions notifications");

        Instant timestamp = Instant.now().minus(1L, ChronoUnit.DAYS);

        videoService.findUsersWithPendingActions(timestamp).forEach(user -> {
            Map<VideoStatus, Long> counts = videoService.findVideosPendingActions(user.getUsername(), timestamp).stream()
                    .collect(groupingBy(Video::getStatus, counting()));
            mailService.sendVideosPendingActionsNotification(user.getUsername(), user.getEmail(), counts);
        });

        log.info("Sending videos pending actions notifications finished");
    }
}
