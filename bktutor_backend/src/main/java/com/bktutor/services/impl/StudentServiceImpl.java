package com.bktutor.services.impl;

import com.bktutor.common.dtos.BookingDto;
import com.bktutor.common.dtos.RecentProgressDto;
import com.bktutor.common.dtos.StudentDashboardDto;
import com.bktutor.common.entity.Booking;
import com.bktutor.common.entity.SessionLog;
import com.bktutor.common.entity.Student;
import com.bktutor.common.enums.BookingStatus;
import com.bktutor.common.enums.ErrorMessage;
import com.bktutor.converter.BookingConverter;
import com.bktutor.exception.NotFoundException;
import com.bktutor.repository.BookingRepository;
import com.bktutor.repository.SessionLogRepository;
import com.bktutor.repository.UserRepository;
import com.bktutor.services.StudentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final SessionLogRepository sessionLogRepository;
    private final BookingConverter bookingConverter;

    public StudentServiceImpl(UserRepository userRepository, BookingRepository bookingRepository, SessionLogRepository sessionLogRepository, BookingConverter bookingConverter) {
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.sessionLogRepository = sessionLogRepository;
        this.bookingConverter = bookingConverter;
    }

    @Override
    public StudentDashboardDto getDashboardData(String studentUsername) {
        Student student = (Student) userRepository.findByUsername(studentUsername)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND));

        Long studentId = student.getId();

        long upcomingCount = bookingRepository.countByStudentIdAndStatus(studentId, BookingStatus.CONFIRMED);
        long totalCount = bookingRepository.countByStudentIdAndStatusNot(studentId, BookingStatus.CANCELED);
        long completedCount = bookingRepository.countByStudentIdAndStatus(studentId, BookingStatus.COMPLETED);
        long tutorCount = bookingRepository.countDistinctTutorsByStudentId(studentId);

        double attendanceRate = (totalCount > 0) ? ((double) completedCount / totalCount) * 100 : 0.0;

        List<Booking> upcomingBookings = bookingRepository.findTop5ByStudentIdAndStatusOrderBySlot_StartTimeAsc(studentId, BookingStatus.CONFIRMED);
        List<BookingDto> upcomingSessionDtos = bookingConverter.convertEntitiesToDTOs(upcomingBookings);

        List<SessionLog> recentLogs = sessionLogRepository.findRecentLogsByStudentId(studentId, 2);
        List<RecentProgressDto> recentProgressDtos = recentLogs.stream().map(log -> {
            RecentProgressDto dto = new RecentProgressDto();
            dto.setSubject(log.getBooking().getSubject());
            dto.setTutorName(log.getBooking().getSlot().getTutor().getFullName());
            dto.setLastNote(log.getTutorNotes());
            return dto;
        }).collect(Collectors.toList());

        StudentDashboardDto dashboardDto = new StudentDashboardDto();
        dashboardDto.setUpcomingSessionsCount(upcomingCount);
        dashboardDto.setTotalSessionsCount(totalCount);
        dashboardDto.setCurrentTutorsCount(tutorCount);
        dashboardDto.setAttendanceRate(Math.round(attendanceRate * 10.0) / 10.0);
        dashboardDto.setUpcomingSessions(upcomingSessionDtos);
        dashboardDto.setRecentProgress(recentProgressDtos);

        return dashboardDto;
    }
}
