package com.volunteerplatform.web;

import com.volunteerplatform.model.enums.Level;
import com.volunteerplatform.service.UserService;
import com.volunteerplatform.web.dto.UserRegisterDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserRegisterController {
    private final UserService userService;

    @GetMapping("/register")
    public String viewRegister(Model model) {
        if (!model.containsAttribute("registerData")) {
            model.addAttribute("registerData", new UserRegisterDTO());
        }
        model.addAttribute("levels", Level.values());
        return "register";
    }


    @PostMapping("/register")
    public String doRegister(
            @Valid @ModelAttribute("registerData") UserRegisterDTO data,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("registerData", data);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerData", bindingResult);
            redirectAttributes.addFlashAttribute("levels", Level.values());
            return "redirect:/users/register";
        }

        userService.register(data);
        return "redirect:/users/login";
    }
}
