package com.bktutor.converter;


import com.bktutor.common.dtos.AvailabilitySlotDto;
import com.bktutor.common.entity.AvailabilitySlot;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component("availabilitySlotConverter")
public class AvailabilitySlotConverter extends SuperConverter<AvailabilitySlotDto, AvailabilitySlot> {

    private final ModelMapper modelMapper;

    public AvailabilitySlotConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public AvailabilitySlotDto convertToDTO(AvailabilitySlot entity) {
        return modelMapper.map(entity, AvailabilitySlotDto.class);
    }

    @Override
    public AvailabilitySlot convertToEntity(AvailabilitySlotDto dto) {
        return modelMapper.map(dto, AvailabilitySlot.class);
    }
}
