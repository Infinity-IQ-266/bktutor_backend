package com.bktutor.controller;

import com.bktutor.common.dtos.MaterialDownloadResponse;
import com.bktutor.common.dtos.S3ObjectResponse;
import com.bktutor.common.dtos.ShareMaterialRequest;
import com.bktutor.common.entity.Material;
import com.bktutor.response.Response;
import com.bktutor.services.MaterialService;
import com.bktutor.utils.SecurityUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/materials")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('TUTOR')")
    public Response uploadMaterial(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("subject") String subject,
            @RequestParam(value = "description", required = false) String description
    ) throws IOException {
        String username = SecurityUtil.getUsername();
        return new Response(materialService.uploadMaterial(username, file, title, subject, description));
    }

    @GetMapping("/tutor/me")
    @PreAuthorize("hasRole('TUTOR')")
    public Response getMyUploadedMaterials() {
        String username = SecurityUtil.getUsername();
        return new Response(materialService.getMyMaterials(username));
    }

    @PostMapping("/{id}/share")
    @PreAuthorize("hasRole('TUTOR')")
    public Response shareMaterial(@PathVariable Long id, @RequestBody ShareMaterialRequest request) {
        String username = SecurityUtil.getUsername();
        materialService.shareMaterial(username, id, request.getStudentIds());
        return new Response("Material shared successfully");
    }

    @GetMapping("/student/me")
    @PreAuthorize("hasRole('STUDENT')")
    public Response getSharedMaterials() {
        String username = SecurityUtil.getUsername();
        return new Response(materialService.getSharedWithMe(username));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadMaterial(@PathVariable Long id) {
        String username = SecurityUtil.getUsername();
        MaterialDownloadResponse file = materialService.downloadMaterial(id, username);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getOriginalFilename() + "\"")
                .body(file.getData());
    }

}