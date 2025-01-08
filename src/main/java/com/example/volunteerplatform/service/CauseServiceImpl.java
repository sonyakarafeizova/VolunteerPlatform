package com.example.volunteerplatform.service;

import com.example.volunteerplatform.data.CauseRepository;
import com.example.volunteerplatform.model.Cause;

import java.util.List;

public class CauseServiceImpl implements CauseService{
    private CauseRepository causeRepository;

    @Override
    public Cause createCause(Cause cause) {
        return causeRepository.save(cause);
    }

    @Override
    public List<Cause> getAllCauses() {
        return causeRepository.findAll();
    }
}
