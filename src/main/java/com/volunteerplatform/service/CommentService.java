package com.volunteerplatform.service;

import com.volunteerplatform.data.CommentRepository;
import com.volunteerplatform.model.Comment;
import com.volunteerplatform.service.dtos.CreateCommentApiDTO;
import com.volunteerplatform.web.dto.CreateCommentDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final UserHelperService userHelperService;
    private CauseHelperService causeHelperService;

    public void create(CreateCommentDTO createCommentDTO) {
        createInternal(createCommentDTO);
    }

    public CreateCommentApiDTO createApi(CreateCommentDTO createCommentDTO) {
        return modelMapper.map(createInternal(createCommentDTO), CreateCommentApiDTO.class);
    }

    public void delete(Long id) {
        commentRepository.deleteById(id);
    }

    private Comment createInternal(CreateCommentDTO createCommentDTO) {
        Comment comment = new Comment();

        comment.setContent(createCommentDTO.getMessage());
        comment.setCause(causeHelperService.getByIdOrThrow(createCommentDTO.getCauseId()));
        comment.setAuthor(userHelperService.getUser());
        comment.setCreated(Instant.now());

        return commentRepository.save(comment);
    }

}
