package com.volunteerplatform.web;

import com.volunteerplatform.model.Cause;
import com.volunteerplatform.model.User;
import com.volunteerplatform.service.CauseService;
import com.volunteerplatform.service.UserService;
import com.volunteerplatform.service.dtos.CauseDetailsDTO;
import com.volunteerplatform.service.dtos.UserProfileDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

import static com.volunteerplatform.model.enums.UserRoles.ADMIN;

@Controller
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;
    private final CauseService causeService;


    @GetMapping("/users/{id}/profile")
    public String viewProfile(@PathVariable("id") Long id, Model model) {
        UserProfileDto profileData = userService.getUserProfileById(id);
        CauseDetailsDTO causes = causeService.getCauseById(id);
        model.addAttribute("profileData", profileData);
        model.addAttribute("causes", causes);
        return "profile";
    }


    @GetMapping("/users/dashboard")
    public String dashboard(Principal principal) {
        User user = userService.findByUsername(principal.getName());

        if (user.getRoles().contains(ADMIN)) {
            return "redirect:/users";
        }

        return "redirect:/users/profile";
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

    @GetMapping("/users/{id}/profile-edit")
    public String editUserInformation(@PathVariable("id") Long id, Model model) {

        if (!model.containsAttribute("userProfileDto")) {
            UserProfileDto userProfileDto = userService.getUserEditDetails(id);
            model.addAttribute("userProfileDto", userProfileDto);
        }
        return "profile-edit";
    }

    @PutMapping("/users/profile")
    public String updateProfile(@Valid @RequestBody UserProfileDto updatedUserDto,
                                BindingResult bindingResult,
                                Principal principal,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userProfileDto", updatedUserDto);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.userEditDto", bindingResult);
            return "redirect:/users/profile-edit";
        }

        userService.updateUserProfile(updatedUserDto);
        return "redirect:/users/profile";
    }

    @PutMapping("/users/{id}/profile-edit")
    public String update(@PathVariable("id") Long id,
                         @Valid @ModelAttribute UserProfileDto userProfileDto,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {

        UserProfileDto existingUser = userService.getUserEditDetails(id);
        if (!bindingResult.hasFieldErrors("username") &&
                !userProfileDto.getUsername().equalsIgnoreCase(existingUser.getUsername()) &&
                userService.usernameExists(userProfileDto.getUsername())) {
            bindingResult.addError(new FieldError("userProfileDto", "username",
                    "This username is already taken!"));
        }

        if (!bindingResult.hasFieldErrors("email") &&
                !userProfileDto.getEmail().equals(existingUser.getEmail()) &&
                userService.emailExists(userProfileDto.getEmail())) {
            bindingResult.addError(new FieldError("userProfileDto", "email",
                    "This email is already taken!"));
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userProfileDto", userProfileDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userProfileDto", bindingResult);
            return "redirect:/users/" + id + "/profile-edit";
        }

        userService.updateUserProfile(userProfileDto);
        return "redirect:/users/" + id + "/profile";
    }
}



