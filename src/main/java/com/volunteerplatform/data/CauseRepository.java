package com.volunteerplatform.data;

import com.volunteerplatform.model.Cause;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CauseRepository extends JpaRepository<Cause, Long> {
}
