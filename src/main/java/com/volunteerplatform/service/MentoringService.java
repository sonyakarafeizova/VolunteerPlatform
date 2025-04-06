package com.volunteerplatform.service;

import com.volunteerplatform.data.CommentRepository;
import com.volunteerplatform.data.MentoringRepository;
import com.volunteerplatform.data.UserRepository;
import com.volunteerplatform.model.Comment;
import com.volunteerplatform.model.Mentoring;
import com.volunteerplatform.model.User;
import com.volunteerplatform.web.dto.CreateCommentDTO;
import com.volunteerplatform.web.dto.MentoringCreateDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MentoringService {

    private final MentoringRepository mentoringRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final UserHelperService userHelperService;
    private final ModelMapper modelMapper;

    public Mentoring createMentoring(MentoringCreateDTO dto) {
        Mentoring mentoring = modelMapper.map(dto, Mentoring.class);
        mentoring.setAuthor(userHelperService.getUser());
        return mentoringRepository.save(mentoring);
    }

@Transactional
public Comment addComment(CreateCommentDTO dto) {
    User user = userHelperService.getUser();

    Mentoring mentoring = mentoringRepository.findById(dto.getMentoringId())
            .orElseThrow(() -> new EntityNotFoundException("Mentoring not found"));

    Comment comment = new Comment();
    comment.setTextContent(dto.getMessage());
    comment.setCreated(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
    comment.setApproved(false);
    comment.setAuthor(user);
    comment.setMentoring(mentoring);

    return commentRepository.save(comment);
}

    @Transactional
    public void addToFavourites(Long mentoringId, User user) {
        Mentoring mentoring = mentoringRepository.findById(mentoringId)
                .orElseThrow(() -> new EntityNotFoundException("Mentoring not found"));

        mentoring.addToFavourites(user);
        mentoringRepository.save(mentoring);
    }

    public boolean add(MentoringCreateDTO data) throws IOException {
        Mentoring toInsert = modelMapper.map(data, Mentoring.class);
        toInsert.setAuthor(userHelperService.getUser());
        mentoringRepository.save(toInsert);
        return false;
    }

    public Set<Mentoring> getFavourites(User user) {
        return user.getFavouriteMentorings();
    }

    public List<Mentoring> getAllMentorings() {
        return mentoringRepository.findAll();
    }

    public Mentoring getMentoringById(Long id) {
        return mentoringRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mentoring not found with id: " + id));
    }
}
