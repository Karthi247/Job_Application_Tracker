package com.pratice.Job_Application_Tracker.Repository;

import com.pratice.Job_Application_Tracker.domain.entity.Interview;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InterviewRepository extends JpaRepository<Interview, Long>, JpaSpecificationExecutor<Interview> {
    Optional<Interview> findByIdAndApplication_User_Id(Long id, Long userId);
    Page<Interview> findByApplication_User_Id(Long userId, Pageable pageable);
    Page<Interview> findByApplication_User_IdAndInterviewDateBetween(Long userId, LocalDate from, LocalDate to, Pageable pageable);
    List<Interview> findByApplication_User_IdAndInterviewDateBetweenOrderByInterviewDateAscInterviewTimeAsc(Long userId, LocalDate from, LocalDate to);
    long countByApplication_User_Id(Long userId);
    Page<Interview> findByApplication_User_IdAndInterviewDateGreaterThanEqualOrderByInterviewDateAscInterviewTimeAsc(
        Long userId, LocalDate from, Pageable pageable);

    @Query("select count(distinct i.application.id) from Interview i where i.application.user.id = :userId")
    long countDistinctApplicationsWithInterviews(@Param("userId") Long userId);
}
