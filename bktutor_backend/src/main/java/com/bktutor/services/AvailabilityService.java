package com.bktutor.services;

import com.bktutor.common.dtos.AvailabilitySlotDto;
import com.bktutor.common.dtos.UpdateAvailabilityDto;
import com.bktutor.common.enums.ErrorMessage;
import jakarta.validation.Valid;

import java.util.List;

public interface AvailabilityService {
    List<AvailabilitySlotDto> updateTutorAvailability(@Valid UpdateAvailabilityDto request, String username);

    List<AvailabilitySlotDto> getMyAvailability(String username);

    List<AvailabilitySlotDto> getAvailableSlotsByTutorId(Long tutorId);
}
