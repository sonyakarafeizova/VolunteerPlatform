package com.volunteerplatform.web;

import com.volunteerplatform.service.CommentService;
import com.volunteerplatform.web.dto.CreateCommentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
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

        return new ModelAndView("redirect:/cause/" + createCommentDTO.getCauseId());
    }

    @PostMapping("comments/delete/{causeId}/{id}")
    public ModelAndView delete(@PathVariable Long causeId, @PathVariable Long id) {
        commentService.delete(id);

        return new ModelAndView("redirect:/cause/" + causeId);
    }
}
