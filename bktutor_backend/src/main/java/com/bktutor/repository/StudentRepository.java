package com.bktutor.repository;

import com.bktutor.common.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @EntityGraph(attributePaths = {"department"})
    Optional<Student> findByUsername(String username);

    //    @EntityGraph(value = "student-with-department", type = EntityGraph.EntityGraphType.FETCH)
    //    Page<Student> findByAssignedTutorId(Long tutorId, Pageable pageable);
    @Query("SELECT DISTINCT b.student FROM Booking b WHERE b.slot.tutor.id = :tutorId")
    Page<Student> findStudentsByTutorId(@Param("tutorId") Long tutorId, Pageable pageable);

    @Query("SELECT s FROM Student s WHERE s.studentId IN :studentIds")
    List<Student> findByStudentIds(@Param("studentIds") List<Long> studentIds);

}
