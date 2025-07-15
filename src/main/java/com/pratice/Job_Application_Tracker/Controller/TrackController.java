package com.pratice.Job_Application_Tracker.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pratice.Job_Application_Tracker.Entity.ApplicationEntity;
import com.pratice.Job_Application_Tracker.Entity.TrackEntity;
import com.pratice.Job_Application_Tracker.Entity.UserEntity;
import com.pratice.Job_Application_Tracker.Repository.ApplicationRepository;
import com.pratice.Job_Application_Tracker.Repository.TrackRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class TrackController {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private TrackRepository trackRepository;

    @GetMapping("/track-info")
    public String viewTracks(HttpSession session, HttpServletRequest request) {
        UserEntity user = (UserEntity) session.getAttribute("user");

        if (user != null) {
            List<TrackEntity> tracks = trackRepository.findByUser_Name(user.getName());
            // Sort by latest added first (assuming you have 'updatedAt' or 'id')
            tracks.sort((a, b) -> b.getId() - a.getId()); // sort descending by ID
            request.setAttribute("tracks", tracks);
        } else {
            request.setAttribute("tracks", List.of());
        }

        return "TrackPage";
    }



    @PostMapping("/save-track")
    public String saveTrack(@ModelAttribute("track") TrackEntity track,
                            @RequestParam("applicationId") int appId,
                            HttpSession session) {

        UserEntity user = (UserEntity) session.getAttribute("user");

        ApplicationEntity app = applicationRepository.findById(appId).orElse(null);
        if (app != null && user != null) {
            track.setTitle(app.getTitle());
            track.setStatus(app.getStatus());
            track.setUser(user);
            trackRepository.save(track);
        }

        return "redirect:/track-info";
    }
    
    @GetMapping("/track-form")
    public String showForm() {
        return "track-form";
    }

    @PostMapping("/add-track")
    public String addTrack(@RequestParam String title,
                           @RequestParam String status,
                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate interviewDate,
                           @RequestParam String company,
                           @RequestParam String jobTitle,
                           @RequestParam String notes,
                           @RequestParam String position,
                           @RequestParam String progress,
                           @RequestParam String stage,
                           HttpSession session) {

        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login"; // User not logged in
        }

        TrackEntity track = new TrackEntity();
        track.setUser(user); // 💡 Required to set user_id
        track.setUserName(user.getName());
        track.setCompany(company);
        track.setJobTitle(jobTitle);
        track.setNotes(notes);
        track.setPosition(position);
        track.setProgress(progress);
        track.setStage(stage);
        track.setStatus(status);
        track.setTitle(title);
        track.setInterviewDate(interviewDate);
        track.setUpdatedAt(LocalDateTime.now());

        trackRepository.save(track);
        return "redirect:/track-info";
    }


}
