package com.bktutor.repository;

import com.bktutor.common.entity.Booking;
import com.bktutor.common.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {

    Page<Booking> findByStudentIdAndStatus(Long studentId, BookingStatus status, Pageable pageable);

    Page<Booking> findBySlot_TutorIdAndStatus(Long tutorId, BookingStatus status, Pageable pageable);

    Page<Booking> findByStudentId(Long studentId, Pageable pageable);

    Page<Booking> findBySlot_TutorId(Long tutorId, Pageable pageable);

    // Đếm số booking của student theo status
    long countByStudentIdAndStatus(Long studentId, BookingStatus status);

    // Đếm tổng số booking của student (không tính CANCELED)
    long countByStudentIdAndStatusNot(Long studentId, BookingStatus status);

    // Lấy N booking sắp tới (status = CONFIRMED)
    List<Booking> findTop5ByStudentIdAndStatusOrderBySlot_StartTimeAsc(Long studentId, BookingStatus status);

    // Đếm số tutor riêng biệt mà student đã học
    @Query("SELECT COUNT(DISTINCT b.slot.tutor) FROM Booking b WHERE b.student.id = :studentId")
    long countDistinctTutorsByStudentId(@Param("studentId") Long studentId);
}
