package com.volunteerplatform.web;

import com.volunteerplatform.model.Level;
import com.volunteerplatform.service.CauseService;
import com.volunteerplatform.service.dtos.CauseShortInfoDTO;
import com.volunteerplatform.web.dto.AddCauseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CauseController {

    private final CauseService causeService;

    @GetMapping("/causes")
    public String causes(Model model) {
        List<CauseShortInfoDTO> causes = causeService.getAllCauses();

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


    @PostMapping("/add-cause")
    public String doAddCause(
           @ModelAttribute AddCauseDTO data,
           @RequestParam("image") MultipartFile file,
            BindingResult bindingResult,
           RedirectAttributes redirectAttributes) throws IOException{
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid form submission!");
            return "redirect:/add-cause";
        }

        causeService.add(data, file);
        redirectAttributes.addFlashAttribute("successMessage", "Cause added successfully!");
        return "redirect:/dashboard";
    }




        @GetMapping("cause/{id}")
        public ModelAndView details (@PathVariable Long id){

            ModelAndView modelAndView = new ModelAndView("causes-details");

            modelAndView.addObject("cause", causeService.getDetails(id));

            return modelAndView;
        }

    }
