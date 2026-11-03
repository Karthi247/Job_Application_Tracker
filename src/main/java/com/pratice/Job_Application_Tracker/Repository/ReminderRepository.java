package com.pratice.Job_Application_Tracker.Repository;

import com.pratice.Job_Application_Tracker.domain.entity.Reminder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    Optional<Reminder> findByIdAndUser_Id(Long id, Long userId);
    Page<Reminder> findByUser_Id(Long userId, Pageable pageable);
    List<Reminder> findByUser_IdAndCompletedAtIsNullAndRemindAtBetweenOrderByRemindAtAsc(Long userId, LocalDateTime from, LocalDateTime to);
    List<Reminder> findTop100ByCompletedAtIsNullAndRemindAtBeforeAndLastNotifiedAtIsNullOrderByRemindAtAsc(LocalDateTime cutoff);
}
