package com.pratice.Job_Application_Tracker.Repository;

import com.pratice.Job_Application_Tracker.domain.entity.JobApplication;
import com.pratice.Job_Application_Tracker.enums.ApplicationStatus;
import com.pratice.Job_Application_Tracker.dto.CompanyCountProjection;
import com.pratice.Job_Application_Tracker.dto.MonthlyCountProjection;
import com.pratice.Job_Application_Tracker.dto.StatusCountProjection;
import java.time.LocalDate;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long>, JpaSpecificationExecutor<JobApplication> {
    Optional<JobApplication> findByIdAndUser_Id(Long id, Long userId);
    Page<JobApplication> findAllByUser_Id(Long userId, Pageable pageable);
    Page<JobApplication> findAll(Specification<JobApplication> specification, Pageable pageable);
    long countByUser_Id(Long userId);
    long countByUser_IdAndStatus(Long userId, ApplicationStatus status);
    Page<JobApplication> findByUser_IdAndCompanyNameContainingIgnoreCaseOrUser_IdAndJobTitleContainingIgnoreCase(
        Long companyUserId, String company, Long titleUserId, String title, Pageable pageable);
    Page<JobApplication> findByUser_IdAndStatus(Long userId, ApplicationStatus status, Pageable pageable);
    Page<JobApplication> findByUser_IdAndPriority(Long userId, com.pratice.Job_Application_Tracker.enums.ApplicationPriority priority, Pageable pageable);
    Page<JobApplication> findByUser_IdAndAppliedDateBetween(Long userId, LocalDate from, LocalDate to, Pageable pageable);
    List<JobApplication> findByUser_IdAndDeadlineBetween(Long userId, LocalDate from, LocalDate to);
    Page<JobApplication> findByUser_IdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    Page<JobApplication> findByUser_IdAndDeadlineGreaterThanEqualOrderByDeadlineAsc(Long userId, LocalDate from, Pageable pageable);

    @Query("select a.status as status, count(a) as count from JobApplication a where a.user.id = :userId group by a.status")
    List<StatusCountProjection> countByStatusForUser(@Param("userId") Long userId);

    @Query("select function('year', a.appliedDate) as year, function('month', a.appliedDate) as month, count(a) as count " +
        "from JobApplication a where a.user.id = :userId and a.appliedDate between :from and :to " +
        "group by function('year', a.appliedDate), function('month', a.appliedDate)")
    List<MonthlyCountProjection> countByMonthForUser(@Param("userId") Long userId,
        @Param("from") LocalDate from, @Param("to") LocalDate to);

    @Query("select a.companyName as companyName, count(a) as count from JobApplication a " +
        "where a.user.id = :userId group by a.companyName order by count(a) desc")
    Page<CompanyCountProjection> countByCompanyForUser(@Param("userId") Long userId, Pageable pageable);
}
