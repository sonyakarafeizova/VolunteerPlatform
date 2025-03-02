package com.volunteerplatform.data;

import com.volunteerplatform.model.Cause;
import com.volunteerplatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CauseRepository extends JpaRepository<Cause, Long> {
    List<Cause> findAll();

    List<Cause> findByAuthor(User author);
}
