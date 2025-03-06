package com.volunteerplatform.data;

import com.volunteerplatform.model.Cause;
import com.volunteerplatform.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CauseRepository extends JpaRepository<Cause, Long> {
    List<Cause> findAll();

    List<Cause> findByAuthor(User author);
    @Transactional
    int deleteByCreatedBefore(LocalDateTime date);
}
