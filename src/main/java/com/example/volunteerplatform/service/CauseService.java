package com.example.volunteerplatform.service;

import com.example.volunteerplatform.model.Cause;

import java.util.List;

public interface CauseService {
    Cause createCause(Cause cause);
    List<Cause> getAllCauses();
}
