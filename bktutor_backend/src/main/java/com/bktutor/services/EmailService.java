package com.bktutor.services;

import com.bktutor.common.entity.Booking;

public interface EmailService {
    void sendNewBookingNotificationToTutor(Booking newBooking);
}