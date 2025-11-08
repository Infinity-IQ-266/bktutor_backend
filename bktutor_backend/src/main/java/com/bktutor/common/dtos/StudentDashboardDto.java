package com.bktutor.common.dtos;

import lombok.Data;

import java.util.List;

@Data
public class StudentDashboardDto {
    private long upcomingSessionsCount;
    private long totalSessionsCount;
    private long currentTutorsCount;
    private double attendanceRate;

    private List<BookingDto> upcomingSessions;
    private List<RecentProgressDto> recentProgress;
}
