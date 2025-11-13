package com.bktutor.controller;

import com.bktutor.common.dtos.AvailabilitySlotDto;
import com.bktutor.common.dtos.UpdateAvailabilityDto;
import com.bktutor.response.Response;
import com.bktutor.services.AvailabilityService;
import com.bktutor.utils.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class AvailabilityController {
    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @PutMapping("/tutors/me/availability")
    public Response updateMyAvailability(@Valid @RequestBody UpdateAvailabilityDto request) {
        String username = SecurityUtil.getUsername();
        return new Response(availabilityService.updateTutorAvailability(request, username));
    }
    @GetMapping("/tutors/me/availability")
    public List<AvailabilitySlotDto> getMyAvailability() {
        String username = SecurityUtil.getUsername();
        return availabilityService.getMyAvailability(username);
    }

    @GetMapping("/tutors/{tutorId}/availability")
    public Response getTutorAvailableSlots(@PathVariable Long tutorId) {
        return new Response(availabilityService.getAvailableSlotsByTutorId(tutorId));
    }
}
