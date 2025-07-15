package com.pratice.Job_Application_Tracker.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pratice.Job_Application_Tracker.Entity.TrackEntity;

@Repository
public interface TrackRepository extends JpaRepository<TrackEntity, Integer> {
    List<TrackEntity> findByUser_Name(String name);  // This is CORRECT here
}
