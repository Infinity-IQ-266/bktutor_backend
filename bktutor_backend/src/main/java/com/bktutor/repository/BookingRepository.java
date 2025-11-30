package com.bktutor.repository;

import com.bktutor.common.entity.Booking;
import com.bktutor.common.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {

    Page<Booking> findByStudentIdAndStatus(Long studentId, BookingStatus status, Pageable pageable);

    Page<Booking> findBySlot_TutorIdAndStatus(Long tutorId, BookingStatus status, Pageable pageable);

    Page<Booking> findByStudentId(Long studentId, Pageable pageable);

    Page<Booking> findBySlot_TutorId(Long tutorId, Pageable pageable);

    long countByStudentIdAndStatus(Long studentId, BookingStatus status);

    long countByStudentIdAndStatusNot(Long studentId, BookingStatus status);

    List<Booking> findTop5ByStudentIdAndStatusOrderBySlot_StartTimeAsc(Long studentId, BookingStatus status);

    @Query("SELECT COUNT(DISTINCT b.slot.tutor) FROM Booking b WHERE b.student.id = :studentId")
    long countDistinctTutorsByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT COUNT(b) FROM Booking b " +
            "WHERE b.slot.tutor.id = :tutorId " +
            "AND b.status = :status " +
            "AND b.slot.startTime >= :startOfDay " +
            "AND b.slot.startTime < :endOfDay")
    long countSessionsByTutorAndDate(
            @Param("tutorId") Long tutorId,
            @Param("status") BookingStatus status,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );

    @Query("SELECT COUNT(DISTINCT b.student) FROM Booking b WHERE b.slot.tutor.id = :tutorId AND b.status IN ('CONFIRMED', 'COMPLETED')")
    long countActiveStudentsByTutorId(@Param("tutorId") Long tutorId);

    long countBySlot_TutorIdAndStatus(Long tutorId, BookingStatus status);

    @EntityGraph(attributePaths = {
            "student",
            "slot",
    })
    List<Booking> findTop5BySlot_TutorIdAndStatusOrderBySlot_StartTimeAsc(Long tutorId, BookingStatus status);
}
