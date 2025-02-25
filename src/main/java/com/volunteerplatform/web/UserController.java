package com.volunteerplatform.web;

import com.volunteerplatform.model.Level;
import com.volunteerplatform.service.UserService;
import com.volunteerplatform.web.dto.UserLoginDTO;
import com.volunteerplatform.web.dto.UserRegisterDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/users/register")
    public String viewRegister(Model model) {
        if (!model.containsAttribute("registerData")) {
            model.addAttribute("registerData", new UserRegisterDTO());
        }

        model.addAttribute("levels", Level.values());

        return "register";
    }


    @PostMapping("/users/register")
    public String doRegister(
            @Valid @ModelAttribute("registerData") UserRegisterDTO data,
            BindingResult bindingResult,
           Model model
    ) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("levels", Level.values());
            return "register";
        }
        if (!model.containsAttribute("registerData")) {
            model.addAttribute("registerData", new UserRegisterDTO());
        }

        userService.register(data);
        return "redirect:/users/login";
    }


    @GetMapping("users/login")
    public ModelAndView viewLogin(@RequestParam(value = "error", required = false) String error) {
        ModelAndView modelAndView = new ModelAndView("login");

        modelAndView.addObject("loginData", new UserLoginDTO());

        if (error != null) {
            modelAndView.addObject("showErrorMessage", true);
        }

        return modelAndView;
    }
    @PostMapping("/users/login")
    public String doLogin(@Valid UserLoginDTO loginData, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("loginData", loginData);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.loginData", bindingResult);
            return "redirect:/users/login";
        }

        boolean loginSuccessful = userService.authenticateUser(loginData);

        if (!loginSuccessful) {
            return "redirect:/users/login?error=true";
        }
        return "redirect:/users/dashboard";


    }



    @GetMapping("users/login-error")
    public ModelAndView viewLoginError() {
        ModelAndView modelAndView = new ModelAndView("login");

        modelAndView.addObject("showErrorMessage", true);
        modelAndView.addObject("loginData", new UserLoginDTO());

        return modelAndView;
    }


    @GetMapping("users/profile")
    public ModelAndView profile(Principal principal) {
        ModelAndView modelAndView = new ModelAndView("profile");
        String username = principal.getName();
        modelAndView.addObject("profileData", userService.getProfileDataByUsername(username));

        return modelAndView;
    }

    @GetMapping("/users/dashboard")
    public ModelAndView dashboard() {
        ModelAndView modelAndView = new ModelAndView("dashboard");

        modelAndView.addObject("profileData", userService.getProfileData());

        return modelAndView;
    }
}
