package com.volunteerplatform.web;

import com.volunteerplatform.service.CauseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final CauseService causeService;

    @GetMapping("/")
    public String index(Model model) {

        model.addAttribute("causes", causeService.getAllCauses());

        return "index";
    }



    @GetMapping("/about")
    public ModelAndView about() {
        return new ModelAndView("about");
    }

    @GetMapping("/access-denied")
    public ModelAndView accessDenied() {
        return new ModelAndView("about");
    }

}
