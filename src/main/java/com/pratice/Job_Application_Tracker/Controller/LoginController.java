package com.pratice.Job_Application_Tracker.Controller;

import com.pratice.Job_Application_Tracker.Entity.UserEntity;
import com.pratice.Job_Application_Tracker.Repository.MainRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private MainRepository userRepository;


    @GetMapping("/login")
    public String loginPage() {
        return "Login";  
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "Signup";
    }
    
    @GetMapping("/home")
    public String homwPage() {
        return "Home";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name,
                           @RequestParam String email,
                           @RequestParam String phone,
                           @RequestParam String password,
                           @RequestParam String confirmPassword) {

        if (!password.equals(confirmPassword)) {
            return "redirect:/signup?error=password_mismatch";
        }

        if (userRepository.findByEmail(email).isPresent() || userRepository.findByPhone(phone).isPresent()) {
            return "redirect:/signup?error=user_exists";
        }

        UserEntity user = new UserEntity();
        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(password);

        userRepository.save(user);
        return "redirect:/login?signup=success";
    }

    @GetMapping("/loginsu")
    public String login(@RequestParam String identifier,
                        @RequestParam String password,
                        HttpSession session) {

        Optional<UserEntity> userOpt = identifier.contains("@")
                ? userRepository.findByEmail(identifier)
                : userRepository.findByPhone(identifier);

        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            if (password.equals(user.getPassword())) {
                session.setAttribute("user", user);
                return "redirect:/home";
            }
        }

        return "redirect:/login?error=invalid";
    }
    
    @GetMapping("/dashboard")
    public String dashboard() {
            return "Dashboard";
    }
    @GetMapping("/track")
    public String track() {
            return "TrackPage";
    }
//    @GetMapping("/applications")
//    public String applications() {
//            return "Applications";
//    }
    
    @GetMapping("/profile")
    public String profile() {
            return "Profile";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout=true";
    }
}
