package com.bktutor.services.impl;

import com.bktutor.common.dtos.AvailabilitySlotDto;
import com.bktutor.common.dtos.UpdateAvailabilityDto;
import com.bktutor.common.entity.AvailabilitySlot;
import com.bktutor.common.entity.Tutor;
import com.bktutor.common.enums.ErrorMessage;
import com.bktutor.common.enums.SlotStatus;
import com.bktutor.converter.AvailabilitySlotConverter;
import com.bktutor.exception.BadRequestException;
import com.bktutor.exception.NotFoundException;
import com.bktutor.repository.AvailabilitySlotRepository;
import com.bktutor.repository.TutorRepository;
import com.bktutor.services.AvailabilityService;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AvailabilityServiceImpl implements AvailabilityService {

    private final AvailabilitySlotRepository availabilitySlotRepository;
    private final AvailabilitySlotConverter availabilitySlotConverter;
    private final TutorRepository tutorRepository;

    public AvailabilityServiceImpl(AvailabilitySlotRepository availabilitySlotRepository, AvailabilitySlotConverter availabilitySlotConverter, TutorRepository tutorRepository) {
        this.availabilitySlotRepository = availabilitySlotRepository;
        this.availabilitySlotConverter = availabilitySlotConverter;
        this.tutorRepository = tutorRepository;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('TUTOR')")
    public List<AvailabilitySlotDto> updateTutorAvailability(UpdateAvailabilityDto dto, String tutorUsername) {
        Tutor currentTutor = tutorRepository.findByUsername(tutorUsername)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND, "Tutor profile not found for current user"));

        availabilitySlotRepository.deleteByTutorIdAndStatus(currentTutor.getId(), SlotStatus.AVAILABLE);

        LocalDateTime now = LocalDateTime.now();
        List<AvailabilitySlot> newSlots = new ArrayList<>();
        for (UpdateAvailabilityDto.TimeSlot timeSlot : dto.getSlots()) {
            if (!timeSlot.getStartTime().isBefore(timeSlot.getEndTime())) {
                throw new BadRequestException(ErrorMessage.BAD_REQUEST, "Start time must be before end time.");
            }
            if (timeSlot.getStartTime().isBefore(now)) {
                throw new BadRequestException(ErrorMessage.BAD_REQUEST, "Cannot create availability slots in the past.");
            }
            for (AvailabilitySlot existing : newSlots) {
                if (timeSlot.getStartTime().isBefore(existing.getEndTime()) &&
                        timeSlot.getEndTime().isAfter(existing.getStartTime())) {
                    throw new BadRequestException(ErrorMessage.BAD_REQUEST, "Time slots cannot overlap.");
                }
            }
            AvailabilitySlot newSlot = new AvailabilitySlot();
            newSlot.setTutor(currentTutor);
            newSlot.setStartTime(timeSlot.getStartTime());
            newSlot.setEndTime(timeSlot.getEndTime());
            newSlot.setStatus(SlotStatus.AVAILABLE);
            newSlots.add(newSlot);
        }

        List<AvailabilitySlot> savedSlots = availabilitySlotRepository.saveAll(newSlots);
        return availabilitySlotConverter.convertEntitiesToDTOs(savedSlots);
    }

    @Override
    @PreAuthorize("hasRole('TUTOR')")
    public List<AvailabilitySlotDto> getMyAvailability(String tutorUsername) {
        Tutor currentTutor = tutorRepository.findByUsername(tutorUsername)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND, "Tutor profile not found for current user"));
        List<AvailabilitySlot> slots = availabilitySlotRepository.findByTutorId(currentTutor.getId());
        return availabilitySlotConverter.convertEntitiesToDTOs(slots);
    }

    @Override
    public List<AvailabilitySlotDto> getAvailableSlotsByTutorId(Long tutorId) {
        if (!tutorRepository.existsById(tutorId)) {
            throw new NotFoundException(ErrorMessage.USER_NOT_FOUND, "Tutor not found");
        }
        List<AvailabilitySlot> availableSlots = availabilitySlotRepository.findByTutorIdAndStatus(tutorId, SlotStatus.AVAILABLE);
        return availabilitySlotConverter.convertEntitiesToDTOs(availableSlots);
    }


}
