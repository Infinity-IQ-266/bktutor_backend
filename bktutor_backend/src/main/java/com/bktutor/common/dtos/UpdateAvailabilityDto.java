package com.bktutor.common.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UpdateAvailabilityDto {

    @NotEmpty
    @Valid
    private List<TimeSlot> slots;

    @Data
    public static class TimeSlot {
        @NotNull
        private LocalDateTime startTime;
        @NotNull
        private LocalDateTime endTime;
    }
}
