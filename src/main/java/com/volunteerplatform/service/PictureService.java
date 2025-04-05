package com.volunteerplatform.service;

import com.volunteerplatform.data.CauseRepository;
import com.volunteerplatform.data.PictureRepository;
import com.volunteerplatform.model.Picture;
import com.volunteerplatform.web.dto.UploadPictureDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PictureService {

    private final PictureRepository pictureRepository;
    private final UserHelperService userHelperService;
    private final CauseRepository causeRepository;
    private final CloudinaryService cloudinaryService;
    private final CauseHelperService causeHelperService;

    public void create(UploadPictureDTO uploadPictureDTO){
        String path=cloudinaryService.upload(uploadPictureDTO.getPicture(),"");
        Picture picture=new Picture();
        picture.setUrl(path);
        picture.setTitle(uploadPictureDTO.getTitle());
        picture.setCause(causeHelperService.getCauseDetailsById(uploadPictureDTO.getCauseId()));
        picture.setAuthor(userHelperService.getUser());

        pictureRepository.save(picture);
    }

    public List<Picture> getPicturesByCauseId(Long causeId) {
        return pictureRepository.findAllByCauseId(causeId);
    }

}
