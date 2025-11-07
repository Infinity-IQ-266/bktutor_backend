package com.bktutor.common.dtos;

import com.bktutor.common.enums.DirectionEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TutorSearchDto {
    private int page;
    private int size;
    private DirectionEnum direction;
    private String attribute;

    private String name;
    private String department;

}
