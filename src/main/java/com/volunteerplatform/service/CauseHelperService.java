package com.volunteerplatform.service;

import com.volunteerplatform.data.CauseRepository;
import com.volunteerplatform.exception.CauseNotFoundException;
import com.volunteerplatform.model.Cause;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CauseHelperService {
    private final CauseRepository causeRepository;

    public Cause getByIdOrThrow(Long id) {
        return causeRepository.findById(id).orElseThrow(() -> new CauseNotFoundException("Cause with id " + id + " not found"));
    }
}
