package com.volunteerplatform.web;

import com.volunteerplatform.model.Picture;
import com.volunteerplatform.model.enums.Level;
import com.volunteerplatform.service.CauseService;
import com.volunteerplatform.service.CloudinaryService;
import com.volunteerplatform.service.PictureService;
import com.volunteerplatform.service.UserService;
import com.volunteerplatform.service.dtos.CauseDetailsDTO;
import com.volunteerplatform.service.dtos.CauseShortInfoDTO;
import com.volunteerplatform.web.dto.AddCauseDTO;
import jakarta.validation.Valid;
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
    private final PictureService pictureService;
    private final CloudinaryService cloudinaryService;
    private final UserService userService;

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
            @Valid @ModelAttribute("causeData")AddCauseDTO data,
            BindingResult bindingResult,
            @RequestParam("image") MultipartFile file,
            Model model,
            RedirectAttributes redirectAttributes) throws IOException {

        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Invalid form submission! Please check your input.");
            return "redirect:/error-add-cause";
        }
        if (file != null && !file.isEmpty()) {
            String imageUrl = cloudinaryService.upload(file, "causes");
            data.setImageUrl(imageUrl);
        } else {

            data.setImageUrl(null);
        }


        causeService.createCause(data);
        redirectAttributes.addFlashAttribute("successMessage", "Cause added successfully!");
        return "redirect:/dashboard";
    }

    @GetMapping("/causes/{id}")
    public String getCauseDetails(@PathVariable Long id, Model model) {
        CauseDetailsDTO causeDetails = causeService.getCauseById(id);
        model.addAttribute("causes", causeDetails);
        List<Picture> pictures = pictureService.getPicturesByCauseId(id);
        model.addAttribute("pictures", pictures);

        return "causes-details";

    }

    @GetMapping("/causes/{id}/delete")
    public String deleteCause(@PathVariable("id") Long id) {
        causeService.deleteCauseFromApi(id);
        return "redirect:/causes";
    }

}
