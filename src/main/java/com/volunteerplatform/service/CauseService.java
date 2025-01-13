package com.volunteerplatform.service;

import com.volunteerplatform.data.CauseRepository;
import com.volunteerplatform.model.Cause;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CauseService {
    private final CauseRepository causeRepository;
    private final ModelMapper modelMapper;
    private final UserHelperService userHelperService;
    public Cause createCause(Cause cause) {
        return causeRepository.save(cause);
    }

    public List<Cause> getAllCauses() {
        return causeRepository.findAll();
    }

    public Cause getCauseById(Long id) {
        Optional<Cause> cause = causeRepository.findById(id);
        return cause.orElse(null);
    }

    public void deleteCause(Long id) {
        causeRepository.deleteById(id);
    }
}