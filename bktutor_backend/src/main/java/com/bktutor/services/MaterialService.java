package com.bktutor.services;

import com.bktutor.common.dtos.MaterialDto;
import com.bktutor.common.enums.ErrorMessage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MaterialService {
    MaterialDto uploadMaterial(String username, MultipartFile file, String title, String subject, String description) throws IOException;

    List<MaterialDto> getMyMaterials(String username);

    void shareMaterial(String username, Long id, List<Long> studentIds);

    List<MaterialDto> getSharedWithMe(String username);

    byte[] downloadMaterial(Long id, String username);
}
