package com.volunteerplatform.web;

import com.volunteerplatform.model.Cause;
import com.volunteerplatform.service.CauseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/causes")
public class CauseController {
    private final CauseService causeService;

    @GetMapping
    public String listCauses(Model model) {
        List<Cause> causes = causeService.getAllCauses();
        model.addAttribute("causes", causes);
        return "cause/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("cause", new Cause());
        return "cause/create";
    }

    @PostMapping("/create")
    public String createCause(@Valid @ModelAttribute Cause cause, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "cause/create";
        }
        causeService.createCause(cause);
        redirectAttributes.addFlashAttribute("message", "Cause created successfully!");
        return "redirect:/causes";
    }

    @GetMapping("/{id}")
    public String viewCause(@PathVariable Long id, Model model) {
        Cause cause = causeService.getCauseById(id);
        if (cause == null) {
            return "redirect:/causes";
        }
        model.addAttribute("cause", cause);
        return "cause/view";
    }

    @PostMapping("/delete/{id}")
    public String deleteCause(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        causeService.deleteCause(id);
        redirectAttributes.addFlashAttribute("message", "Cause deleted successfully!");
        return "redirect:/causes";
    }
}