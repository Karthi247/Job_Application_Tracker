package com.pratice.Job_Application_Tracker.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pratice.Job_Application_Tracker.Entity.ApplicationEntity;
import com.pratice.Job_Application_Tracker.Entity.UserEntity;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Integer> {

    // Get all applications by user
    List<ApplicationEntity> findByUser(UserEntity user);

    // Dashboard-specific methods
    int countByUser(UserEntity user);

    int countByStatusIgnoreCaseAndUser(String status, UserEntity user);

    List<ApplicationEntity> findTop5ByUserOrderByIdDesc(UserEntity user);
}
