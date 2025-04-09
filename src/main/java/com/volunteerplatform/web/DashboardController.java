package com.volunteerplatform.web;

import com.volunteerplatform.service.CauseService;
import com.volunteerplatform.service.UserService;
import com.volunteerplatform.service.dtos.CauseShortInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final CauseService causeService;
    private final UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<CauseShortInfoDTO> causes = causeService.getAllCauses();
        model.addAttribute("allCauses", causes);
        model.addAttribute("profileData",userService.getProfileData());
        return "dashboard";
    }
}