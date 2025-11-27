package com.bktutor.common.dtos;

import lombok.Data;

@Data
public class MaterialDto {
    private Long id;
    private String title;
    private String description;
    private String fileType;
    private String size;
    private String uploadDate;
    private String source;
    private String subjectName;
    private int sharedWithCount;
    private String downloadUrl;
}