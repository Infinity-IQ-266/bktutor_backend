package com.bktutor.repository;

import com.bktutor.common.entity.SessionLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionLogRepository extends JpaRepository<SessionLog, Long> {

    @Query("SELECT sl FROM SessionLog sl " +
            "JOIN FETCH sl.booking b " +
            "JOIN FETCH b.slot s " +
            "JOIN FETCH s.tutor " +
            "WHERE b.student.id = :studentId " +
            "ORDER BY sl.createdAt DESC")
    List<SessionLog> findRecentLogsByStudentId(@Param("studentId") Long studentId, Pageable pageable);

}
