package az.edu.turing.service.impl;

import az.edu.turing.dao.BookingRepository;
import az.edu.turing.dao.FlightsRepository;
import az.edu.turing.dao.entity.BookingEntity;
import az.edu.turing.dao.entity.FlightsEntity;
import az.edu.turing.exception.BookingNotFoundException;
import az.edu.turing.exception.FlightNotFoundException;
import az.edu.turing.exception.InvalidMenuActionException;
import az.edu.turing.model.BookingDto;
import az.edu.turing.service.BookingService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

public class BookingServicePostgresImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final FlightsRepository flightsRepository;

    public BookingServicePostgresImpl(BookingRepository bookingRepository, FlightsRepository flightsRepository) {
        this.bookingRepository = bookingRepository;
        this.flightsRepository = flightsRepository;
    }

    @Override
    public void bookFlight(BookingDto bookingDto, int numberofseats) {
        Collection<BookingEntity> bookings = new ArrayList<>();
        Predicate<FlightsEntity> predicate = flightsEntity -> flightsEntity.getFlightId() == bookingDto.getFlightId();
        Optional<FlightsEntity> f = flightsRepository.findOneBy(predicate);

        if (f.isEmpty()) {
            throw new FlightNotFoundException("No such flight");
        } else {
            int seats = f.get().getSeats();
            if (numberofseats > seats) throw new InvalidMenuActionException("Number of seats exceeded");
            BookingEntity bookingEntity = new BookingEntity(bookingDto.getFlightId(), bookingDto.getPassengerNames());
            seats -= numberofseats;
            f.get().setSeats(seats);
            bookings.add(bookingEntity);
            bookingRepository.save(bookings);
            flightsRepository.update(f.get());
        }
    }

    @Override
    public void cancelBooking(long bookingId) {
        Predicate<BookingEntity> predicate = bookingEntity -> bookingEntity.getTicketId() == bookingId;
        Optional<BookingEntity> optionalBooking = bookingRepository.findOneBy(predicate);

        if (optionalBooking.isEmpty()) {
            throw new InvalidMenuActionException("No such booking");
        } else {
            BookingEntity booking = optionalBooking.get();
            Predicate<FlightsEntity> flightPredicate = flightsEntity -> flightsEntity.getFlightId() == booking.getFlightId();
            Optional<FlightsEntity> optionalFlight = flightsRepository.findOneBy(flightPredicate);

            if (optionalFlight.isEmpty()) {
                throw new InvalidMenuActionException("No such flight for the booking");
            } else {
                FlightsEntity flight = optionalFlight.get();
                int numberOfPassengers = booking.getPassengerNames().size();
                flight.setSeats(flight.getSeats() + numberOfPassengers);

                bookingRepository.delete(bookingId);
                flightsRepository.update(flight);

                System.out.println("Booking cancelled successfully!");
            }
        }
    }


    @Override
    public Collection<BookingEntity> getBookingsByPassenger(String fullName) {
        Predicate<BookingEntity> predicate = bookingEntity -> bookingEntity.getPassengerNames().contains(fullName);
        Collection<BookingEntity> bookings = bookingRepository.findAllBy(predicate);
        if (!bookings.isEmpty()) {
            return bookings;
        } else {
            throw new BookingNotFoundException("No such passenger");
        }
    }
}
