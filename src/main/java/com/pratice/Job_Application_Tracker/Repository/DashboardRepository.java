package com.pratice.Job_Application_Tracker.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pratice.Job_Application_Tracker.Entity.DashboardEntity;

public interface DashboardRepository extends JpaRepository<DashboardEntity, Integer> {
    DashboardEntity findByUserName(String userName);
}
