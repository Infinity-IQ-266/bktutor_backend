package com.bktutor.services;

import com.bktutor.common.dtos.S3ObjectResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface S3Service {

    String uploadFile(MultipartFile file) throws IOException;

    S3ObjectResponse downloadFile(String key);

}
