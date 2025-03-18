package com.volunteerplatform.web;

import com.volunteerplatform.data.CauseRepository;
import com.volunteerplatform.model.Cause;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminController {
    private final CauseRepository causeRepository;

    public AdminController(CauseRepository causeRepository) {
        this.causeRepository = causeRepository;
    }
   @GetMapping("/admin")
    public String adminDashboard(Model model, @AuthenticationPrincipal UserDetails loggedUser){
        List<Cause> causes=causeRepository.findAll();
        model.addAttribute("causes",causes);
        model.addAttribute("username",loggedUser.getUsername());
        return "admin";
    }
}
