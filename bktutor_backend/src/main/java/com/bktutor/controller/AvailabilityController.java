package com.bktutor.controller;

import com.bktutor.common.dtos.UpdateAvailabilityDto;
import com.bktutor.response.Response;
import com.bktutor.services.AvailabilityService;
import com.bktutor.utils.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

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
    public Response getMyAvailability() {
        String username = SecurityUtil.getUsername();
        return new Response(availabilityService.getMyAvailability(username));
    }

    @GetMapping("/tutors/{tutorId}/availability")
    public Response getTutorAvailableSlots(@PathVariable Long tutorId) {
        return new Response(availabilityService.getAvailableSlotsByTutorId(tutorId));
    }
}
