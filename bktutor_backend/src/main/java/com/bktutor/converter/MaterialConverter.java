package com.bktutor.converter;

import com.bktutor.common.dtos.MaterialDto;
import com.bktutor.common.entity.Material;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component("materialConverter")
public class MaterialConverter extends SuperConverter<MaterialDto, Material> {

    private final ModelMapper modelMapper;

    public MaterialConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public MaterialDto convertToDTO(Material entity) {
        MaterialDto dto = modelMapper.map(entity, MaterialDto.class);
        dto.setSize(formatFileSize(entity.getFileSize()));
        dto.setDownloadUrl("/api/v1/materials/" + entity.getId() + "/download");
        return dto;
    }

    @Override
    public Material convertToEntity(MaterialDto dto) {
        return modelMapper.map(dto, Material.class);
    }

    private String formatFileSize(Long size) {
        if (size == null) return "0 B";
        double mb = size / (1024.0 * 1024.0);
        return String.format("%.1f MB", mb);
    }
}
