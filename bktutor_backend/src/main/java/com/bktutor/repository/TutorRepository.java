package com.bktutor.repository;

import com.bktutor.common.entity.Tutor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TutorRepository extends JpaRepository<Tutor, Long>, JpaSpecificationExecutor<Tutor> {

    @Override
    @EntityGraph(attributePaths = {"department"})
    Optional<Tutor> findById(Long id);

    Optional<Tutor> findByUsername(String username);

}
