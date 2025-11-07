package com.bktutor.repository;

import com.bktutor.common.entity.AvailabilitySlot;
import com.bktutor.common.enums.SlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvailabilitySlotRepository extends JpaRepository<AvailabilitySlot, Long> {
    List<AvailabilitySlot> findByTutorId(Long tutorId);

    List<AvailabilitySlot> findByTutorIdAndStatus(Long tutorId, SlotStatus status);

    void deleteByTutorIdAndStatus(Long tutorId, SlotStatus status);
}
