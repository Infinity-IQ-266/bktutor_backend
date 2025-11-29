package com.bktutor.common.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class S3ObjectResponse {
    private byte[] data;
    private String contentType;
}

