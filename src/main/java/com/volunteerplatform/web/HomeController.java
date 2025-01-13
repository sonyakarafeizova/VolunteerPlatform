package com.volunteerplatform.web;

import com.volunteerplatform.service.CauseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final CauseService causeService;

    @GetMapping("/")
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("causes", causeService.getAllCauses());
        return modelAndView;
    }



    @GetMapping("/about")
    public ModelAndView index() {
        return new ModelAndView("about");
    }

    @GetMapping("/access-denied")
    public ModelAndView accessDenied() {
        return new ModelAndView("about");
    }
}
