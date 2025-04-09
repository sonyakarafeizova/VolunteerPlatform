package com.volunteerplatform.web;


import com.volunteerplatform.data.MentoringRepository;
import com.volunteerplatform.data.UserRepository;
import com.volunteerplatform.model.Mentoring;
import com.volunteerplatform.model.User;
import com.volunteerplatform.service.MentoringService;
import com.volunteerplatform.service.UserHelperService;
import com.volunteerplatform.web.dto.CreateCommentDTO;
import com.volunteerplatform.web.dto.MentoringCreateDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/mentoring")
@RequiredArgsConstructor
public class MentoringController {

    private final MentoringService mentoringService;
    private final MentoringRepository mentoringRepository;
    private final UserHelperService userHelperService;
    private final UserRepository userRepository;

    @GetMapping
    @Transactional
    public String listMentorings(Model model) {
        List<Mentoring> allMentorings = mentoringRepository.findAllWithComments();
        model.addAttribute("mentoring", allMentorings);

        User currentUser = userHelperService.getUser();
        if (currentUser != null) {
            currentUser=userRepository.findById(currentUser.getId()).orElse(currentUser);
            model.addAttribute("favourites", new ArrayList<>(currentUser.getFavouriteMentorings()));
        } else {
            model.addAttribute("favourites", Collections.emptyList());
        }

        return "mentoring";
    }


    @ModelAttribute("mentoringData")
    public MentoringCreateDTO mentoringData() {
        return new MentoringCreateDTO();

    }

    @GetMapping("/create")
    public String createMentoringForm(Model model) {
        model.addAttribute("mentoringData", new MentoringCreateDTO());
        return "mentoring-create";
    }


    @PostMapping("/create")
    public String createMentoring(@Valid MentoringCreateDTO dto,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) throws IOException {
        if (bindingResult.hasErrors()) {
            return "mentoring-create";
        }
        mentoringService.add(dto);
        return "redirect:/mentoring";
    }


    @PostMapping("/{id}/favourite")
    public String addToFavourites(@PathVariable Long id) {
        User user = userHelperService.getUser();
        if (user == null) {
            System.out.println("User is null, cannot add to favourites");
            return "redirect:/users/login";
        }
        System.out.println("Adding mentoring with id " + id + " to favourites for user " + user.getUsername());
        mentoringService.addToFavourites(id, user);
        return "redirect:/mentoring";
    }

    @PostMapping("/{id}/comment")
    public String postComment(@PathVariable("id") Long mentoringId,
                              @RequestParam("content") String content) {
        User currentUser = userHelperService.getUser();
        if (currentUser == null) {
            return "redirect:/users/login";
        }

        CreateCommentDTO dto = new CreateCommentDTO();
        dto.setMentoringId(mentoringId);
        dto.setAuthor(currentUser.getUsername());
        dto.setContent(content);

        mentoringService.addComment(dto);

        return "redirect:/mentoring";
    }

    @PostMapping("/{id}/unfavourite")
    public String removeFromFavouritesMapping(@PathVariable Long id) {
        User user = userHelperService.getUser();
        if (user == null) {
            return "redirect:/users/login";
        }
        mentoringService.removeFromFavourites(id, user);
        return "redirect:/mentoring";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteMentoring(@PathVariable Long id) {
        mentoringService.deleteMentoring(id);
        return "redirect:/mentoring";
    }
    @DeleteMapping("/comment/{commentId}/delete")
    public String deleteComment(@PathVariable Long commentId) {
        mentoringService.deleteComment(commentId);
        return "redirect:/mentoring";
    }

}
