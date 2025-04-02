package com.volunteerplatform.web;

import com.volunteerplatform.model.User;
import com.volunteerplatform.model.enums.UserRoles;
import com.volunteerplatform.service.UserService;
import com.volunteerplatform.service.session.AppUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")

public class AdminController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView getAllUsers(@AuthenticationPrincipal AppUserDetailsService appUserDetailsService) {

        List<User> users = userService.getAllUsers();
        List<String> roles = Arrays.asList("USER", "ADMIN", "MODERATOR");
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("users");
        modelAndView.addObject("users", users);
        modelAndView.addObject("roles", roles);

        return modelAndView;
    }


    @GetMapping("users/edit/{id}")
    public String editUserRole(@PathVariable Long id, Model model) {
        User user = userService.findUserById(id);
        List<String> roles = Arrays.asList("USER", "ADMIN", "MODERATOR");

        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return "users";
    }

    @PutMapping("users/edit/{id}")
    public String updateUserRoleForm(@PathVariable("id") Long id, @RequestParam("role") String roleName) {

        UserRoles role = UserRoles.valueOf(roleName.toUpperCase());
        userService.updateUserRole(id, role);

        return "redirect:/admin";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
