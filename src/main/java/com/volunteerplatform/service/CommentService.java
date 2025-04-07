package com.volunteerplatform.service;

import com.volunteerplatform.data.CommentRepository;
import com.volunteerplatform.model.Comment;
import com.volunteerplatform.service.dtos.CreateCommentApiDTO;
import com.volunteerplatform.web.dto.CreateCommentDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final UserHelperService userHelperService;
    private final UserService userService;
    private final MentoringService mentoringService;


//    Comment createInternal(CreateCommentDTO dto) {
//        final var comment = new Comment();
//        comment.setTextContent(dto.getContent());
//        comment.setCreated(Instant.now());
//        comment.setAuthor(userHelperService.getUserDetails(dto.getAuthorId()));
//        comment.setApproved(false); // ensure unapproved by default
//        comment.setMentoring(mentoringService.getMentoringById(dto.getMentoringId()));
//
//        return commentRepository.save(comment);
//    }
//}



    @Transactional
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


        comment.setTextContent(createCommentDTO.getContent());
        comment.setMentoring(mentoringService.getMentoringById(createCommentDTO.getMentoringId()));
        comment.setAuthor(userHelperService.getUser());
        comment.setCreated(Instant.now());

        return commentRepository.save(comment);
    }

}
