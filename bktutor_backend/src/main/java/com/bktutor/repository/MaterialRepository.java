package com.bktutor.repository;

import com.bktutor.common.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    List<Material> findByTutorId(Long id);

    List<Material> findByRecipientsId(Long id);
}
