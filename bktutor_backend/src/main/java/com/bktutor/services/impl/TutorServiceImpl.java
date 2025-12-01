package com.bktutor.services.impl;

import com.bktutor.common.dtos.*;
import com.bktutor.common.entity.Booking;
import com.bktutor.common.entity.Feedback;
import com.bktutor.common.entity.Student;
import com.bktutor.common.entity.Tutor;
import com.bktutor.common.enums.BookingStatus;
import com.bktutor.common.enums.ErrorMessage;
import com.bktutor.converter.BookingConverter;
import com.bktutor.converter.FeedbackConverter;
import com.bktutor.converter.TutorConverter;
import com.bktutor.converter.UserConverter;
import com.bktutor.exception.NotFoundException;
import com.bktutor.repository.BookingRepository;
import com.bktutor.repository.FeedbackRepository;
import com.bktutor.repository.StudentRepository;
import com.bktutor.repository.TutorRepository;
import com.bktutor.services.TutorService;
import com.bktutor.specification.TutorSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TutorServiceImpl implements TutorService {

    private final TutorRepository tutorRepository;
    private final BookingRepository bookingRepository;
    private final FeedbackRepository feedbackRepository;
    private final TutorConverter tutorConverter;
    private final BookingConverter bookingConverter;
    private final StudentRepository studentRepository;
    private final UserConverter userConverter;
    private final FeedbackConverter feedbackConverter;

    public TutorServiceImpl(TutorRepository tutorRepository, BookingRepository bookingRepository, TutorConverter tutorConverter, FeedbackRepository feedbackRepository, BookingConverter bookingConverter, StudentRepository studentRepository, UserConverter userConverter, FeedbackConverter feedbackConverter) {
        this.tutorRepository = tutorRepository;
        this.bookingRepository = bookingRepository;
        this.tutorConverter = tutorConverter;
        this.feedbackRepository = feedbackRepository;
        this.bookingConverter = bookingConverter;
        this.studentRepository = studentRepository;
        this.userConverter = userConverter;
        this.feedbackConverter = feedbackConverter;
    }

    @Override
    public Page<TutorDto> searchTutors(TutorSearchDto searchDto) {
        Specification<Tutor> specification = TutorSpecification.hasNameLike(searchDto.getName())
                .and(TutorSpecification.inDepartment(searchDto.getDepartment()));
        Sort sortable = Sort.by(Sort.Direction.fromString(searchDto.getDirection().name()), searchDto.getAttribute());

        Pageable pageable = PageRequest.of(searchDto.getPage(), searchDto.getSize(), sortable);

        Page<Tutor> tutors = tutorRepository.findAll(specification, pageable);

        return tutors.map(tutorConverter::convertToDTO);
    }

    @Override
    public TutorDto findTutorById(Long tutorId) {
        Tutor tutor = tutorRepository.findById(tutorId).orElseThrow(() -> new NotFoundException(ErrorMessage.TUTOR_NOT_FOUND, "Tutor not found"));
        return tutorConverter.convertToDTO(tutor);
    }

    @Override
    public TutorDashboardDto getDashboardData(String tutorUsername) {
        Tutor tutor = tutorRepository.findByUsername(tutorUsername)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND));
        Long tutorId = tutor.getId();

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

        long todaySessions = bookingRepository.countSessionsByTutorAndDate(
                tutorId,
                BookingStatus.CONFIRMED,
                startOfDay,
                endOfDay
        );

        long activeStudents = bookingRepository.countActiveStudentsByTutorId(tutorId);
        long pendingRequestsCount = bookingRepository.countBySlot_TutorIdAndStatus(tutorId, BookingStatus.PENDING);
        double avgRating = tutor.getAverageRating();

        List<Booking> pendingBookings = bookingRepository.findTop5BySlot_TutorIdAndStatusOrderBySlot_StartTimeAsc(tutorId, BookingStatus.PENDING);
        List<BookingDto> pendingRequestDtos = bookingConverter.convertEntitiesToDTOs(pendingBookings);

        List<Booking> upcomingBookings = bookingRepository.findTop5BySlot_TutorIdAndStatusOrderBySlot_StartTimeAsc(tutorId, BookingStatus.CONFIRMED);
        List<BookingDto> upcomingSessionDtos = bookingConverter.convertEntitiesToDTOs(upcomingBookings);

        List<Feedback> feedbacks = feedbackRepository.findRecentFeedbackByTutorId(tutorId, PageRequest.of(0, 5));
        List<TutorDashboardDto.RecentFeedbackDto> feedbackDtos = feedbacks.stream().map(f -> {
            TutorDashboardDto.RecentFeedbackDto dto = new TutorDashboardDto.RecentFeedbackDto();
            dto.setId(f.getId());
            dto.setStudentName(f.getStudent().getFullName());
            dto.setSubject(f.getBooking().getSubject());
            dto.setRating(f.getRating());
            dto.setDate(f.getCreatedAt().toString());
            dto.setComment(f.getComment());
            return dto;
        }).toList();
        TutorDashboardDto dashboardDto = new TutorDashboardDto();
        dashboardDto.setTodaySessionsCount(todaySessions);
        dashboardDto.setActiveStudentsCount(activeStudents);
        dashboardDto.setPendingRequestsCount(pendingRequestsCount);
        dashboardDto.setAverageRating(avgRating);
        dashboardDto.setPendingRequests(pendingRequestDtos);
        dashboardDto.setUpcomingSessions(upcomingSessionDtos);
        dashboardDto.setRecentFeedback(feedbackDtos);

        return dashboardDto;
    }

    @Override
    public Page<StudentDetailDto> getMyStudents(String username, Pageable pageable) {
        Tutor tutor = tutorRepository.findByUsername(username).
                orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND, "Tutor not found"));
        Page<Student> students = studentRepository.findStudentsByTutorId(tutor.getId(), pageable);
        return students.map(s -> (StudentDetailDto) userConverter.convertToDTO(s));
    }

    @Override
    public Page<FeedbackDto> getMyFeedbacks(String username, Pageable pageable) {
        Tutor tutor = tutorRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND));

        Page<Feedback> feedbacks = feedbackRepository.findByTutorId(tutor.getId(), pageable);

        return feedbacks.map(feedbackConverter::convertToDTO);
    }
}
