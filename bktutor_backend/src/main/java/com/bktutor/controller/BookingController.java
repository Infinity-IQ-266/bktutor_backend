package com.bktutor.controller;

import com.bktutor.common.dtos.BookingSearchDto;
import com.bktutor.common.dtos.CreateBookingDto;
import com.bktutor.common.enums.BookingStatus;
import com.bktutor.common.enums.BookingType;
import com.bktutor.common.enums.DirectionEnum;
import com.bktutor.response.Response;
import com.bktutor.services.BookingService;
import com.bktutor.utils.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public Response createBooking(@Valid @RequestBody CreateBookingDto createDto) {
        String studentUsername = SecurityUtil.getUsername();
        return new Response(bookingService.createBooking(createDto, studentUsername));
    }

    @GetMapping("/me")
    public Response getMyBookings(
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "direction", required = false, defaultValue = "DESC") DirectionEnum direction,
            @RequestParam(name = "attribute", required = false, defaultValue = "createdAt") String attribute,
            @RequestParam(name = "status", required = false) BookingStatus status,
            @RequestParam(name = "subjectName", required = false) String subjectName,
            @RequestParam(name = "bookingType", required = false) BookingType bookingType
    ) {
        String username = SecurityUtil.getUsername();
        return new Response(bookingService.getMyBookings(username,
                BookingSearchDto.builder()
                        .page(page)
                        .size(size)
                        .direction(direction)
                        .attribute(attribute)
                        .status(status)
                        .type(bookingType)
                        .subjectName(subjectName)
                        .build()
                ));
    }

    @GetMapping("/{id}")
    public Response getBookingById(@PathVariable Long id) {
        return new Response(bookingService.findBookingById(id));
    }

    @PostMapping("/{id}/confirm")
    public Response confirmBooking(@PathVariable Long id) {
        String tutorUsername = SecurityUtil.getUsername();
        return new Response(bookingService.confirmBooking(id, tutorUsername));
    }

    @PostMapping("/{id}/reject")
    public Response rejectBooking(@PathVariable Long id) {
        String tutorUsername = SecurityUtil.getUsername();
        bookingService.rejectBooking(id, tutorUsername);
        return new Response("Booking rejected successfully.");
    }

    @PostMapping("/{id}/cancel")
    public Response cancelBooking(@PathVariable Long id) {
        String username = SecurityUtil.getUsername();
        return new Response(bookingService.cancelBooking(id, username));
    }
}
