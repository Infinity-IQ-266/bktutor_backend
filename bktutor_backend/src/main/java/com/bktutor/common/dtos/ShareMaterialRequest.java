package com.bktutor.common.dtos;

import lombok.Data;

import java.util.List;

@Data
public class ShareMaterialRequest {
    private List<Long> studentIds;
}
