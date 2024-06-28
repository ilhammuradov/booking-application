package az.edu.turing.service;

import az.edu.turing.dao.entity.BookingEntity;
import az.edu.turing.model.BookingDto;

import java.util.Collection;

public interface BookingService {

    void bookFlight(BookingDto bookingDto, int numberofseats);

    void cancelBooking(long bookingId);

    Collection<BookingEntity> getBookingsByPassenger(String fullName);
}
