package com.bktutor.common.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MaterialDownloadResponse {
    private byte[] data;
    private String contentType;
    private String originalFilename;
}
