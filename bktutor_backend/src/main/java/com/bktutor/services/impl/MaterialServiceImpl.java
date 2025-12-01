package com.bktutor.services.impl;

import com.bktutor.common.dtos.MaterialDownloadResponse;
import com.bktutor.common.dtos.MaterialDto;
import com.bktutor.common.dtos.S3ObjectResponse;
import com.bktutor.common.entity.Material;
import com.bktutor.common.entity.Student;
import com.bktutor.common.entity.Tutor;
import com.bktutor.common.enums.ErrorMessage;
import com.bktutor.common.enums.MaterialSource;
import com.bktutor.converter.MaterialConverter;
import com.bktutor.exception.NotFoundException;
import com.bktutor.repository.MaterialRepository;
import com.bktutor.repository.StudentRepository;
import com.bktutor.repository.TutorRepository;
import com.bktutor.services.MaterialService;
import com.bktutor.services.S3Service;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;
    private final TutorRepository tutorRepository;
    private final StudentRepository studentRepository;
    private final S3Service s3Service;
    private final MaterialConverter materialConverter;

    public MaterialServiceImpl(MaterialRepository materialRepository, TutorRepository tutorRepository, StudentRepository studentRepository, S3Service s3Service, MaterialConverter materialConverter) {
        this.materialRepository = materialRepository;
        this.tutorRepository = tutorRepository;
        this.studentRepository = studentRepository;
        this.s3Service = s3Service;
        this.materialConverter = materialConverter;
    }

    @Override
    public MaterialDto uploadMaterial(String username, MultipartFile file, String title, String subject, String description) throws IOException {
        Tutor tutor = tutorRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND));
        String s3Key = s3Service.uploadFile(file);

        Material material = new Material();
        material.setTitle(title);
        material.setSubjectName(subject);
        material.setDescription(description);
        material.setFileType(getFileExtension(Objects.requireNonNull(file.getOriginalFilename())));
        material.setFileSize(file.getSize());
        material.setS3Key(s3Key);
        material.setOriginalFilename(file.getOriginalFilename());
        material.setSource(MaterialSource.UPLOADED);
        material.setTutor(tutor);

        Material saved = materialRepository.save(material);
        return materialConverter.convertToDTO(saved);
    }

    @Override
    public List<MaterialDto> getMyMaterials(String tutorUsername) {
        Tutor tutor = tutorRepository.findByUsername(tutorUsername)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND));

        return materialRepository.findByTutorId(tutor.getId()).stream()
                .map(materialConverter::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void shareMaterial(String tutorUsername, Long materialId, List<Long> studentIds) {
        Tutor tutor = tutorRepository.findByUsername(tutorUsername)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND));

        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.MATERIAL_NOT_FOUND, "Material not found"));

        if (!material.getTutor().getId().equals(tutor.getId())) {
            throw new RuntimeException("You do not own this material");
        }

        List<Student> students = studentRepository.findByStudentIds(studentIds);

        material.getRecipients().addAll(students);

        materialRepository.save(material);
    }

    @Override
    public List<MaterialDto> getSharedWithMe(String username) {
        Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND));
        return materialRepository.findByRecipientsId(student.getId()).stream()
                .map(materialConverter::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MaterialDownloadResponse downloadMaterial(Long materialId, String username) {

        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));

        S3ObjectResponse s3Obj = s3Service.downloadFile(material.getS3Key());

        return MaterialDownloadResponse.builder()
                .data(s3Obj.getData())
                .contentType(s3Obj.getContentType())
                .originalFilename(material.getOriginalFilename())
                .build();
    }


    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
