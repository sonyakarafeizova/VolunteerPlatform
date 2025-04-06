package com.volunteerplatform.data;

import com.volunteerplatform.model.Mentoring;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MentoringRepository extends JpaRepository<Mentoring, Long> {
    @Query("SELECT m FROM Mentoring m LEFT JOIN FETCH m.comments")
    List<Mentoring> findAllWithComments();
}