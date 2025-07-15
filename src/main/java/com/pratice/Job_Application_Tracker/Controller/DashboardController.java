package com.pratice.Job_Application_Tracker.Controller;

import com.pratice.Job_Application_Tracker.Entity.ApplicationEntity;
import com.pratice.Job_Application_Tracker.Entity.DashboardEntity;
import com.pratice.Job_Application_Tracker.Entity.UserEntity;
import com.pratice.Job_Application_Tracker.Repository.ApplicationRepository;
import com.pratice.Job_Application_Tracker.Repository.DashboardRepository;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private DashboardRepository dashboardRepository;

    @GetMapping("/dashboard-info") // CHANGED this to match your link
    public String showDashboard(Model model, HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login"; // not logged in
        }

        // Count values using case-insensitive status queries
        int totalApplied = applicationRepository.countByUser(user);
        int interviewsScheduled = applicationRepository.countByStatusIgnoreCaseAndUser("Interviewing", user);
        int offersReceived = applicationRepository.countByStatusIgnoreCaseAndUser("Offer", user);
        int rejections = applicationRepository.countByStatusIgnoreCaseAndUser("Rejected", user);

        // Debugging (Optional)
        System.out.println("User: " + user.getName());
        System.out.println("Interview Scheduled: " + interviewsScheduled);
        System.out.println("Offers Received: " + offersReceived);

        // Set values in dashboard object
        DashboardEntity dashboard = new DashboardEntity();
        dashboard.setUserName(user.getName());
        dashboard.setTotalApplied(totalApplied);
        dashboard.setInterviewsScheduled(interviewsScheduled);
        dashboard.setOffersReceived(offersReceived);
        dashboard.setRejections(rejections);

        // Add to model
        model.addAttribute("dashboard", dashboard);

        // Also send recent 5 applications
        List<ApplicationEntity> recentApps = applicationRepository.findTop5ByUserOrderByIdDesc(user);
        model.addAttribute("recentApps", recentApps);

        return "Dashboard"; // JSP file: Dashboard.jsp
    }
}
