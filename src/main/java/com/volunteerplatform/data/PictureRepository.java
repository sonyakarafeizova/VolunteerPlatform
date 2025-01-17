package com.volunteerplatform.data;

import com.volunteerplatform.model.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Long> {
    Picture findFirstByCause_Id(Long id);
}
