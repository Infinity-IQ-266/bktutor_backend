package com.bktutor.controller;

import com.bktutor.response.Response;
import com.bktutor.services.S3Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/v1")
public class S3Controller {

    private final S3Service s3Service;

    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping("/upload")
    public Response upload(@RequestParam("file") MultipartFile file) throws IOException {
        return new Response(s3Service.uploadFile(file));

    }

    @GetMapping("/download")
    public Response download(@RequestParam String folder, @RequestParam String name) {
        String key = folder + "/" + name;
        return new Response(s3Service.downloadFile(key));
    }
}
