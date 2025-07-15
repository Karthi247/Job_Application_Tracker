package com.pratice.Job_Application_Tracker.Controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.pratice.Job_Application_Tracker.Entity.ApplicationEntity;
import com.pratice.Job_Application_Tracker.Entity.UserEntity;
import com.pratice.Job_Application_Tracker.Repository.ApplicationRepository;

import jakarta.servlet.http.HttpSession;

@Controller

public class ApplicationController {

    @Autowired
    private ApplicationRepository applicationRepo;

    @GetMapping("")
    public String viewApplications(Model model, HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        List<ApplicationEntity> apps = applicationRepo.findByUser(user);
        model.addAttribute("applications", apps);
        return "Applications";
    }


    @GetMapping("/applications/add")
    public String addForm(Model model) {
        model.addAttribute("application", new ApplicationEntity());
        return "ApplicationForm";
    }

    @PostMapping("/applications/save")
    public String saveApplication(@ModelAttribute ApplicationEntity app, HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        // ✅ Set the user and the user_name before saving
        app.setUser(user); // for the @ManyToOne relationship
        app.setUserName(user.getName()); // ✅ explicitly set the user_name column
        
        app.setAppliedDate(LocalDate.now());

        applicationRepo.save(app);
        return "redirect:/applications";
    }
    
    @PostMapping("/applications/update")
    public String updateApplication(ApplicationEntity updatedApp, HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        ApplicationEntity app = applicationRepo.findById(updatedApp.getId()).orElse(null);
        if (app != null && app.getUser().getId() == user.getId()) {
            app.setTitle(updatedApp.getTitle());
            app.setCompany(updatedApp.getCompany());
            app.setStatus(updatedApp.getStatus());
            app.setAppliedDate(updatedApp.getAppliedDate());
            applicationRepo.save(app);
        }

        return "redirect:/applications";
    }


    @GetMapping("/applications/edit/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model, HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        ApplicationEntity app = applicationRepo.findById(id).orElse(null);
        if (app == null || app.getUser().getId() != user.getId()) {
            return "redirect:/applications"; // Not found or not the user's application
        }

        model.addAttribute("application", app);
        return "EditApplication"; // JSP file: EditApplication.jsp
    }

    
    @GetMapping("/applications")
    public String viewApplications1(Model model, HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        List<ApplicationEntity> applications = applicationRepo.findByUser(user);
        model.addAttribute("applications", applications);
        return "Applications"; // your JSP page
    }

    @GetMapping("/applications/delete/{id}")
    public String deleteApplication(@PathVariable("id") int id, HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        // Optional: Check if the application belongs to the current user
        ApplicationEntity app = applicationRepo.findById(id).orElse(null);
        if (app != null && app.getUser().getId() == user.getId()) {
            applicationRepo.deleteById(id);
        }

        return "redirect:/applications"; // Redirect back to applications list
    }

}



