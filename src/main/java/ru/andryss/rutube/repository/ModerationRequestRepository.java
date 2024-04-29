package ru.andryss.rutube.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.andryss.rutube.message.AssignmentInfo;
import ru.andryss.rutube.model.ModerationRequest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ModerationRequestRepository extends JpaRepository<ModerationRequest, String> {
    Optional<ModerationRequest> findBySourceIdAndAssignee(String id, String assignee);

    @Modifying
    @Query(value = """
        update moderation_requests
        set assignee = :username, assigned_at = :assignedAt
        where source_id = :sourceId
    """, nativeQuery = true)
    void assignModeration(String sourceId, String username, Instant assignedAt);

    @Query(value = """
        select new ru.andryss.rutube.message.AssignmentInfo(m.username, m.email, r.sourceId)
        from ModerationRequest r join Moderator m on r.assignee = m.username
        where r.assignee is not null and r.createdAt < :timestamp
    """)
    List<AssignmentInfo> findAssignedBefore(Instant timestamp);
}
