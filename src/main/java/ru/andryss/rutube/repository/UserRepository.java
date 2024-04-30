package ru.andryss.rutube.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.andryss.rutube.model.User;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    @Query(value = """
        select u.id_, u.rev_, u.first_, u.last_, u.email_, u.pwd_, u.salt_, u.lock_exp_time_, u.attempts_, u.picture_id_
        from videos as v join act_id_user as u on v.author = u.id_
        where v.status in ('UPLOAD_PENDING', 'FILL_PENDING') and v.updated_at < :timestamp
    """, nativeQuery = true)
    List<User> findWithVideosPendingActions(Instant timestamp);
}
