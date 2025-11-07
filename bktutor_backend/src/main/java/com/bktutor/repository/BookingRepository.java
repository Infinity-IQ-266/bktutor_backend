package com.bktutor.repository;

import com.bktutor.common.entity.Booking;
import com.bktutor.common.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {

    Page<Booking> findByStudentIdAndStatus(Long studentId, BookingStatus status, Pageable pageable);

    Page<Booking> findBySlot_TutorIdAndStatus(Long tutorId, BookingStatus status, Pageable pageable);

    Page<Booking> findByStudentId(Long studentId, Pageable pageable);

    Page<Booking> findBySlot_TutorId(Long tutorId, Pageable pageable);
}
