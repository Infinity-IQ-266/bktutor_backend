package com.bktutor.services.impl;

import com.bktutor.common.dtos.BookingDto;
import com.bktutor.common.dtos.BookingSearchDto;
import com.bktutor.common.dtos.CreateBookingDto;
import com.bktutor.common.entity.*;
import com.bktutor.common.enums.BookingStatus;
import com.bktutor.common.enums.ErrorMessage;
import com.bktutor.common.enums.SlotStatus;
import com.bktutor.converter.BookingConverter;
import com.bktutor.exception.ConflictException;
import com.bktutor.exception.ForbiddenException;
import com.bktutor.exception.NotFoundException;
import com.bktutor.repository.*;
import com.bktutor.services.BookingService;
import com.bktutor.specification.BookingSpecification;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

import java.util.Objects;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final AvailabilitySlotRepository availabilitySlotRepository;
    private final BookingConverter bookingConverter;

    public BookingServiceImpl(BookingRepository bookingRepository, UserRepository userRepository, AvailabilitySlotRepository availabilitySlotRepository, BookingConverter bookingConverter) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.availabilitySlotRepository = availabilitySlotRepository;
        this.bookingConverter = bookingConverter;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('STUDENT')")
    public BookingDto createBooking(CreateBookingDto createDto, String studentUsername) {
        Student student = (Student) userRepository.findByUsername(studentUsername)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND));

        AvailabilitySlot slot = availabilitySlotRepository.findById(createDto.getSlotId())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.AVAILABILITY_SLOT_NOT_FOUND, "Availability slot not found"));

        if (slot.getStatus() != SlotStatus.AVAILABLE) {
            throw new ConflictException(ErrorMessage.BAD_REQUEST, "This time slot is no longer available.");
        }

        slot.setStatus(SlotStatus.BOOKED);
        availabilitySlotRepository.save(slot);

        Booking newBooking = new Booking();

        newBooking.setStudent(student);
        newBooking.setSlot(slot);
        if (createDto.getSubject() != null) {
            newBooking.setSubject(createDto.getSubject());
        }
        if (createDto.getType() != null) {
            newBooking.setType(createDto.getType());
        }
        if (createDto.getLocationOrLink() != null) {
            newBooking.setLocationOrLink(createDto.getLocationOrLink());
        }

        if (createDto.getStudentNotes() != null) {
            newBooking.setStudentNotes(createDto.getStudentNotes());
        }
        newBooking.setStatus(BookingStatus.PENDING);

        return bookingConverter.convertToDTO(bookingRepository.save(newBooking));
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('TUTOR')")
    public BookingDto confirmBooking(Long bookingId, String tutorUsername) {
        Tutor tutor = (Tutor) userRepository.findByUsername(tutorUsername)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND));

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.BOOKING_NOT_FOUND, "Booking not found"));

        if (!Objects.equals(booking.getSlot().getTutor().getId(), tutor.getId())) {
            throw new ForbiddenException(ErrorMessage.FORBIDDEN_AUTHORITY);
        }

        booking.setStatus(BookingStatus.CONFIRMED);

        return bookingConverter.convertToDTO(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('TUTOR')")
    public void rejectBooking(Long bookingId, String tutorUsername) {
        Tutor tutor = (Tutor) userRepository.findByUsername(tutorUsername)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND));

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.BOOKING_NOT_FOUND, "Booking not found"));

        if (!Objects.equals(booking.getSlot().getTutor().getId(), tutor.getId())) {
            throw new ForbiddenException(ErrorMessage.FORBIDDEN_AUTHORITY);
        }

        AvailabilitySlot slot = booking.getSlot();

        slot.setStatus(SlotStatus.AVAILABLE);

        booking.setStatus(BookingStatus.REJECTED);

        availabilitySlotRepository.save(slot);

        bookingRepository.save(booking);

    }

    @Override
    @Transactional
    public BookingDto cancelBooking(Long bookingId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND));

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.BOOKING_NOT_FOUND, "Booking not found"));

        boolean isParticipant = Objects.equals(booking.getStudent().getId(), user.getId()) ||
                Objects.equals(booking.getSlot().getTutor().getId(), user.getId());

        if (!isParticipant) {
            throw new ForbiddenException(ErrorMessage.FORBIDDEN_AUTHORITY);
        }

        booking.setStatus(BookingStatus.CANCELED);

        AvailabilitySlot slot = booking.getSlot();
        slot.setStatus(SlotStatus.AVAILABLE);
        availabilitySlotRepository.save(slot);

        return bookingConverter.convertToDTO(bookingRepository.save(booking));
    }

    @Override
    public BookingDto findBookingById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.BOOKING_NOT_FOUND, "Booking not found"));
        return bookingConverter.convertToDTO(booking);
    }

    @Override
    public Page<BookingDto> getMyBookings(String username, BookingSearchDto request) {
        Specification<Booking> specification = BookingSpecification.belongsToStudent(username)
                .and(BookingSpecification.hasSubjectLike(request.getSubjectName()))
                .and(BookingSpecification.hasStatus(request.getStatus()))
                .and(BookingSpecification.hasType(request.getType()));
        Sort sortable = Sort.by(Sort.Direction.fromString(request.getDirection().name()), request.getAttribute());

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sortable);

        Page<Booking> bookings = bookingRepository.findAll(specification, pageable);
        return bookings.map(bookingConverter::convertToDTO);
    }
}
