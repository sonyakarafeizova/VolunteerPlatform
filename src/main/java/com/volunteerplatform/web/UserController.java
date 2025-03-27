package com.volunteerplatform.web;

import com.volunteerplatform.data.UserRepository;
import com.volunteerplatform.model.Cause;
import com.volunteerplatform.model.User;
import com.volunteerplatform.model.enums.Level;
import com.volunteerplatform.model.enums.UserRoles;
import com.volunteerplatform.service.CauseService;
import com.volunteerplatform.service.UserService;
import com.volunteerplatform.web.dto.UserLoginDTO;
import com.volunteerplatform.web.dto.UserRegisterDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;
    private final CauseService causeService;
    private final UserRepository userRepository;


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


    @GetMapping("/users/login")
    public ModelAndView viewLogin(@RequestParam(value = "error", required = false) String error) {
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("loginData", new UserLoginDTO());

        if (error != null) {
            modelAndView.addObject("showErrorMessage", true);
        }

        return modelAndView;
    }


    @PostMapping("/users/login")
    public String doLogin(@Valid @ModelAttribute("loginData") UserLoginDTO loginData,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {
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


    @GetMapping("/users/login-error")
    public ModelAndView viewLoginError() {
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("showErrorMessage", true);
        modelAndView.addObject("loginData", new UserLoginDTO());
        return modelAndView;
    }


    @GetMapping("/users/profile")
    public ModelAndView profile(Principal principal) {
        ModelAndView modelAndView = new ModelAndView("profile");
        String username = principal.getName();

        User user = userService.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        List<Cause> userCauses = causeService.findByUser(user);


        modelAndView.addObject("user", user);
        modelAndView.addObject("causes", userCauses);
        modelAndView.addObject("profileData", userService.getProfileData());

        return modelAndView;

    }


    @GetMapping("/users/dashboard")
    public ModelAndView dashboard() {
        ModelAndView modelAndView = new ModelAndView("dashboard");
        modelAndView.addObject("profileData", userService.getProfileData());
        return modelAndView;
    }


    @PutMapping("/users/profile")
    public User updateProfile(@RequestBody User updatedUser) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        currentUser.setFullName(updatedUser.getFullName());
        currentUser.setEmail(updatedUser.getEmail());
        currentUser.setAge(updatedUser.getAge());

        return userRepository.save(currentUser);
    }


    @PutMapping("/users/{userId}/roles")
    public void assignRoles(@PathVariable Long userId, @RequestBody Set<UserRoles> roles) {
        userService.assignRolesToUser(userId, roles);
    }

    // Admin endpoint for removing roles from a user
    @PutMapping("/users/{userId}/remove-roles")
    public void removeRoles(@PathVariable Long userId, @RequestBody Set<UserRoles> roles) {
        userService.removeRolesFromUser(userId, roles);
    }

}