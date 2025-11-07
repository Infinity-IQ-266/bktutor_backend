package com.bktutor.converter;

import com.bktutor.common.dtos.BookingDto;
import com.bktutor.common.entity.Booking;
import com.bktutor.common.entity.Student;
import com.bktutor.common.entity.Tutor;
import com.bktutor.common.entity.AvailabilitySlot;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component("bookingConverter")
public class BookingConverter extends SuperConverter<BookingDto, Booking> {

    private final ModelMapper modelMapper;

    public BookingConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public BookingDto convertToDTO(Booking entity) {
        BookingDto dto = modelMapper.map(entity, BookingDto.class);

        Student student = entity.getStudent();
        if (student != null) {
            dto.setStudentId(student.getId());
            dto.setStudentName(student.getFullName());
        }

        AvailabilitySlot slot = entity.getSlot();
        if (slot != null) {
            dto.setSlotId(slot.getId());
            dto.setStartTime(slot.getStartTime());
            dto.setEndTime(slot.getEndTime());

            Tutor tutor = slot.getTutor();
            if (tutor != null) {
                dto.setTutorId(tutor.getId());
                dto.setTutorName(tutor.getFullName());
            }
        }

        return dto;
    }

    @Override
    public Booking convertToEntity(BookingDto dto) {
        return modelMapper.map(dto, Booking.class);
    }
}
