package com.bktutor.common.dtos;

import com.bktutor.common.enums.BookingStatus;
import com.bktutor.common.enums.BookingType;
import com.bktutor.common.enums.DirectionEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingSearchDto {
    private int page;
    private int size;
    private DirectionEnum direction;
    private String attribute;

    private String subjectName;
    private BookingType type;
    private BookingStatus status;
}
