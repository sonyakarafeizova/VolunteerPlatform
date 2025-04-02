package com.volunteerplatform.web;

import com.volunteerplatform.model.User;
import com.volunteerplatform.service.UserService;
import com.volunteerplatform.web.dto.UserLoginDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

import static com.volunteerplatform.model.enums.UserRoles.ADMIN;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserLoginController {
    private final UserService userService;

    @GetMapping("/login")
    public ModelAndView viewLogin(@RequestParam(value = "error", required = false) String error) {
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("loginData", new UserLoginDTO());

        if (error != null) {
            modelAndView.addObject("showErrorMessage", true);
        }

        return modelAndView;
    }


    @PostMapping("/login")
    public String doLogin(@Valid @ModelAttribute("loginData") UserLoginDTO loginData,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          Principal principal) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("loginData", loginData);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.loginData", bindingResult);
            return "redirect:/users/login";
        }

        boolean loginSuccessful = userService.authenticateUser(loginData);
        if (!loginSuccessful) {
            return "redirect:/users/login?error=true";
        }
        User user = userService.findByUsername(principal.getName());
        if (user.getRoles().contains(ADMIN)) {
            return "redirect:/";
        }

        return "redirect:/users/profile";
    }


    @GetMapping("/login-error")
    public ModelAndView viewLoginError() {
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("showErrorMessage", true);
        modelAndView.addObject("loginData", new UserLoginDTO());
        return modelAndView;
    }

}
