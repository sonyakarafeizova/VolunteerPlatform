package com.volunteerplatform.web;

import com.volunteerplatform.model.Level;
import com.volunteerplatform.service.CauseService;
import com.volunteerplatform.service.dtos.CauseShortInfoDTO;
import com.volunteerplatform.web.dto.AddCauseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CauseController {

    private final CauseService causeService;

    @GetMapping("/causes")
    public String causes(Model model) {
        List<CauseShortInfoDTO> causes = causeService.getAll();

        model.addAttribute("allCauses", causes);

        return "causes";
    }

    @GetMapping("add-cause")
    public ModelAndView addCause() {

        ModelAndView modelAndView = new ModelAndView("add-cause");

        modelAndView.addObject("cause", new CauseShortInfoDTO());
        modelAndView.addObject("levels", Level.values());

        return modelAndView;

    }


    @ModelAttribute("causeData")
    public AddCauseDTO causeData() {
        return new AddCauseDTO();
    }




        @GetMapping("cause/{id}")
        public ModelAndView details (@PathVariable Long id){

            ModelAndView modelAndView = new ModelAndView("causes-details");

            modelAndView.addObject("cause", causeService.getDetails(id));

            return modelAndView;
        }
    }
