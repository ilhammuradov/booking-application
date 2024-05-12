package az.edu.turing.controller;

import az.edu.turing.entity.BookingEntity;
import az.edu.turing.model.BookingDto;
import az.edu.turing.service.BookingService;

import java.util.Collection;

public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public void searchAndBookFlight(BookingDto bookingDto) {
        bookingService.bookFlight(bookingDto);
    }

    public  void cancelBooking(long ticketId) {
        bookingService.cancelBooking(ticketId);
    }

    public Collection<BookingEntity> myFlights(String fullName) {
        return bookingService.getBookingsByPassenger(fullName);
    }
}
