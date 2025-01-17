package com.volunteerplatform.web.rest;

import com.volunteerplatform.service.CommentService;
import com.volunteerplatform.service.dtos.CreateCommentApiDTO;
import com.volunteerplatform.web.dto.CreateCommentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentApiController {
    private final CommentService commentService;

    @PostMapping("/api/comments")
    public CreateCommentApiDTO create(@RequestBody CreateCommentDTO createCommentApiDTO){
        return commentService.createApi(createCommentApiDTO);
    }
}
