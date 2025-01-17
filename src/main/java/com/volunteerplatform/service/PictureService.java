package com.volunteerplatform.service;

import com.volunteerplatform.data.CauseRepository;
import com.volunteerplatform.data.PictureRepository;
import org.springframework.stereotype.Service;

@Service

public class PictureService {
    private final PictureRepository pictureRepository;
    private final UserHelperService userHelperService;
    private final CauseRepository causeRepository;

    public PictureService(PictureRepository pictureRepository, UserHelperService userHelperService, CauseRepository causeRepository) {
        this.pictureRepository = pictureRepository;
        this.userHelperService = userHelperService;
        this.causeRepository = causeRepository;
    }
}
