package com.volunteerplatform.web;

import com.volunteerplatform.service.CauseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Random;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final CauseService causeService;

    @GetMapping("/")
    public String index(Model model) {
        double sofiaTemp = new Random().nextDouble();

        model.addAttribute("sofiaTemperature", sofiaTemp);


        return "index";
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
