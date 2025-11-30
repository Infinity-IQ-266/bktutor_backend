package com.bktutor.common.dtos;

import lombok.Data;

import java.util.List;

@Data
public class TutorDashboardDto {
    private long todaySessionsCount;

    private long activeStudentsCount;

    private long pendingRequestsCount;

    private double averageRating;

    private List<BookingDto> pendingRequests;
    private List<BookingDto> upcomingSessions;
    private List<RecentFeedbackDto> recentFeedback;

    @Data
    public static class RecentFeedbackDto {
        private Long id;
        private String studentName;
        private String subject;
        private int rating;
        private String date;
        private String comment;
    }
}
