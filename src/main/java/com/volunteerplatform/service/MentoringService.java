package com.volunteerplatform.service;

import com.volunteerplatform.data.CommentRepository;
import com.volunteerplatform.data.MentoringRepository;
import com.volunteerplatform.data.UserRepository;
import com.volunteerplatform.exception.MentoringNotFoundException;
import com.volunteerplatform.model.Comment;
import com.volunteerplatform.model.Mentoring;
import com.volunteerplatform.model.User;
import com.volunteerplatform.web.dto.CreateCommentDTO;
import com.volunteerplatform.web.dto.MentoringCreateDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class MentoringService {

    private final MentoringRepository mentoringRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final UserHelperService userHelperService;
    private final ModelMapper modelMapper;

//    public Mentoring createMentoring(MentoringCreateDTO dto) {
//        Mentoring mentoring = modelMapper.map(dto, Mentoring.class);
//        mentoring.setAuthor(userHelperService.getUser());
//        return mentoringRepository.save(mentoring);
//    }

    public Mentoring getById(Long id) {
        return mentoringRepository.findById(id)
                .orElseThrow(() -> new MentoringNotFoundException("Mentoring not found: " + id));
    }

    @Transactional
    public Comment addComment(CreateCommentDTO dto) {
        User user = userHelperService.getUser();

        Mentoring mentoring = mentoringRepository.findById(dto.getMentoringId())
                .orElseThrow(() -> new MentoringNotFoundException("Mentoring not found"));

        Comment comment = new Comment();
        comment.setTextContent(dto.getContent());
        comment.setCreated(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        comment.setApproved(false);
        comment.setAuthor(user);
        comment.setMentoring(mentoring);

        return commentRepository.save(comment);
    }

    @Transactional
    public void addToFavourites(Long mentoringId, User user) {
        Mentoring mentoring = mentoringRepository.findById(mentoringId)
                .orElseThrow(() -> new MentoringNotFoundException("Mentoring not found"));

        mentoring.addToFavourites(user);
        mentoring.getTitle();
        mentoring.getDescription();
        mentoringRepository.save(mentoring);
    }

    public boolean add(MentoringCreateDTO data) throws IOException {
        Mentoring toInsert = modelMapper.map(data, Mentoring.class);
        toInsert.setAuthor(userHelperService.getUser());
        mentoringRepository.save(toInsert);
        return false;
    }

    @Transactional
    public void removeFromFavourites(Long mentoringId, User user) {
        Mentoring mentoring = mentoringRepository.findById(mentoringId)
                .orElseThrow(() -> new MentoringNotFoundException("Mentoring not found"));

        User reloadedUser = userRepository.findByIdWithFavouriteMentorings(user.getId())
                .orElse(user);

        mentoring.removeFromFavourites(reloadedUser);
        mentoringRepository.save(mentoring);
    }

    public Mentoring getMentoringById(Long id) {
        return mentoringRepository.findById(id)
                .orElseThrow(() -> new MentoringNotFoundException("Mentoring not found with id: " + id));
    }

    @Transactional
    public void deleteMentoring(Long id) {

        Mentoring mentoring = mentoringRepository.findById(id)
                .orElseThrow(() -> new MentoringNotFoundException("Mentoring not found with id: " + id));

        mentoringRepository.delete(mentoring);
    }

    @Transactional
    public void deleteComment(Long commentId) {

        commentRepository.deleteById(commentId);

    }

}
