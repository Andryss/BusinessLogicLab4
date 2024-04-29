package ru.andryss.rutube.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.andryss.rutube.service.SourceService;

@RestController
@RequiredArgsConstructor
public class SourceController {

    private final SourceService sourceService;

    @GetMapping(value = "/api/sources/{sourceId}", produces = "video/mp4")
    public byte[] getApiSources(
            @PathVariable String sourceId
    ) {
        return sourceService.getVideo(sourceId);
    }
}
