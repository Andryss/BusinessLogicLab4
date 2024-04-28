package ru.andryss.rutube.message;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AssignmentInfo {
    String assignee;
    String email;
    String sourceId;
}
