package com.bktutor.common.dtos;

import com.bktutor.common.enums.SlotStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AvailabilitySlotDto {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private SlotStatus status;
}
