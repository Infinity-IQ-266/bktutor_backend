package com.bktutor.converter;

import com.bktutor.common.dtos.FeedbackDto;
import com.bktutor.common.entity.Feedback;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("feedbackConverter")
public class FeedbackConverter extends SuperConverter<FeedbackDto, Feedback> {

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public FeedbackDto convertToDTO(Feedback entity) {
        FeedbackDto dto = modelMapper.map(entity, FeedbackDto.class);

        if (entity.getStudent() != null) {
            dto.setStudentId(entity.getStudent().getId());
            dto.setStudentName(entity.getStudent().getFullName());
            dto.setStudentAvatar(entity.getStudent().getAvatarUrl());
        }

        if (entity.getBooking() != null) {
            dto.setSubject(entity.getBooking().getSubject());
        }

        return dto;
    }

    @Override
    public Feedback convertToEntity(FeedbackDto dto) {
        return modelMapper.map(dto, Feedback.class);
    }
}
