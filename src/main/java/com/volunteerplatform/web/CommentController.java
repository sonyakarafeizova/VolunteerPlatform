package com.volunteerplatform.web;

import com.volunteerplatform.service.CommentService;
import com.volunteerplatform.web.dto.CreateCommentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @PostMapping("comments/create")
    public ModelAndView create(CreateCommentDTO createCommentDTO) {
        commentService.create(createCommentDTO);


        return new ModelAndView("redirect:/mentoring/" + createCommentDTO.getMentoringId());
    }
    @PostMapping
    public String postComment(@ModelAttribute CreateCommentDTO dto) {
        commentService.create(dto);
        return "redirect:/mentoring/%s".formatted(dto.getMentoringId());
    }

    @PostMapping("comments/delete/{mentoringId}/{id}")
    public ModelAndView delete(@PathVariable Long mentoringId, @PathVariable Long id) {
        commentService.delete(id);
        return new ModelAndView("redirect:/mentoring/" + mentoringId);
    }


}
