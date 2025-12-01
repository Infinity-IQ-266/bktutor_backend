package com.bktutor.repository;

import com.bktutor.common.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    // Lấy feedback gần đây của tutor
    @Query("SELECT f FROM Feedback f " +
            "JOIN FETCH f.student " +
            "JOIN FETCH f.booking " +
            "JOIN f.booking b " +
            "WHERE b.slot.tutor.id = :tutorId " +
            "ORDER BY f.createdAt DESC")
    List<Feedback> findRecentFeedbackByTutorId(@Param("tutorId") Long tutorId, Pageable pageable);

    @Query("SELECT f FROM Feedback f " +
            "JOIN FETCH f.student s " +
            "JOIN FETCH f.booking b " +
            "JOIN b.slot slot " +
            "WHERE slot.tutor.id = :tutorId")
    Page<Feedback> findByTutorId(@Param("tutorId") Long tutorId, Pageable pageable);
}