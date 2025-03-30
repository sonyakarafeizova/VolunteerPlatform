package com.volunteerplatform.web;

import com.volunteerplatform.data.CauseRepository;
import com.volunteerplatform.model.Cause;
import com.volunteerplatform.model.User;
import com.volunteerplatform.model.enums.UserRoles;
import com.volunteerplatform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final CauseRepository causeRepository;
    private final UserService userService;


   @GetMapping("/")
    public String adminDashboard(Model model, @AuthenticationPrincipal UserDetails loggedUser){
        List<Cause> causes=causeRepository.findAll();
        model.addAttribute("causes",causes);
        model.addAttribute("username",loggedUser.getUsername());
        return "admin";
    }
    @GetMapping("/admin-users")
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        List<String> roles = Arrays.asList("USER", "ADMIN", "MODERATOR");
        model.addAttribute("users", users);
        model.addAttribute("roles", roles);
        return "admin-users";
    }


    @GetMapping("/users/edit/{id}")
    public String editUserRole(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id);
        List<String> roles = Arrays.asList("USER", "ADMIN", "MODERATOR");
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return "edit-user";
    }

    @PostMapping("/users/edit/{id}")
    public String updateUserRole(@PathVariable("id") Long id, @RequestParam("role") UserRoles role) {
        User user = userService.getUserById(id);

        userService.updateUserRole(id, role);
        return "redirect:/admin-users";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteCause(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin-users";
    }

}
