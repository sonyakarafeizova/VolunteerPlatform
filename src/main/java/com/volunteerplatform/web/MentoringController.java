package com.volunteerplatform.web;


import com.volunteerplatform.data.MentoringRepository;
import com.volunteerplatform.model.Comment;
import com.volunteerplatform.model.Mentoring;
import com.volunteerplatform.model.User;
import com.volunteerplatform.service.MentoringService;
import com.volunteerplatform.service.dtos.MentoringDetailsDTO;
import com.volunteerplatform.web.dto.CreateCommentDTO;
import com.volunteerplatform.web.dto.MentoringCreateDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/mentoring")
@RequiredArgsConstructor
public class MentoringController {

    private final MentoringService mentoringService;
    private final MentoringRepository mentoringRepository;

    @GetMapping
    public String listMentorings(Model model) {
        List<Mentoring> mentoring = mentoringRepository.findAllWithComments();
        model.addAttribute("mentoring", mentoring);
        return "mentoring-list";
    }

//    @GetMapping("/create")
//    public ModelAndView createMentoring() {
//        ModelAndView modelAndView = new ModelAndView("mentoring-create");
//        modelAndView.addObject("mentoring", new MentoringDetailsDTO());
//        return modelAndView;
//
//    }

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
    public String addToFavourites(@PathVariable Long id,
                                  @AuthenticationPrincipal User user) {
        mentoringService.addToFavourites(id, user);
        return "redirect:/mentoring";
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<MentoringDetailsDTO> postComment(@RequestParam Long mentoringId,
                                                           @RequestParam String commentText) {


        CreateCommentDTO dto = new CreateCommentDTO();
        dto.setMentoringId(mentoringId);
        dto.setMessage(commentText);
        Comment comment = mentoringService.addComment(dto);

        MentoringDetailsDTO response = new MentoringDetailsDTO();
        response.setId(comment.getId());
        response.setAuthorName(comment.getAuthor().getUsername());
        response.setDescription(comment.getTextContent());


        return ResponseEntity.ok(response);
    }


}
