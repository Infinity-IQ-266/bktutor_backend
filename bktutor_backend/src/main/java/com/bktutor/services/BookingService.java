package com.bktutor.services;

import com.bktutor.common.dtos.BookingDto;
import com.bktutor.common.dtos.BookingSearchDto;
import com.bktutor.common.dtos.CreateBookingDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

public interface BookingService {
    BookingDto createBooking(@Valid CreateBookingDto createDto, String studentUsername);

    BookingDto confirmBooking(Long id, String tutorUsername);

    void rejectBooking(Long id, String tutorUsername);

    BookingDto cancelBooking(Long id, String username);

    BookingDto  findBookingById(Long id);

    Page<BookingDto> getMyBookings(String username, BookingSearchDto build);
}
