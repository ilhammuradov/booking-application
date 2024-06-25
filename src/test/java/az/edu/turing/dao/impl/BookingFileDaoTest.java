//package az.edu.turing.dao.impl;
//
//import az.edu.turing.entity.BookingEntity;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.Optional;
//import java.util.function.Predicate;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class BookingFileDaoTest {
//
//    private BookingFileDao bookingFileDao;
//    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    void setUp() {
//        objectMapper = new ObjectMapper();
//        bookingFileDao = new BookingFileDao(objectMapper);
//    }
//
//    @Test
//    void saveAndGetAll() throws IOException {
//
//        Collection<BookingEntity> bookingsToSave = new ArrayList<>();
//        bookingsToSave.add(new BookingEntity( 1, List.of("Alice", "Bob")));
//        bookingsToSave.add(new BookingEntity( 2, List.of("Charlie", "David")));
//        bookingFileDao.save(bookingsToSave);
//
//        Collection<BookingEntity> savedBookings = bookingFileDao.getAll();
//        assertNotNull(savedBookings);
//        assertEquals(2, savedBookings.size());
//        assertTrue(savedBookings.containsAll(bookingsToSave));
//    }
//
//    @Test
//    void delete() throws IOException {
//
//        long ticketIdToDelete = 1;
//        BookingEntity booking1 = new BookingEntity( 1, List.of("Alice", "Bob"));
//        BookingEntity booking2 = new BookingEntity( 1, List.of("Charlie", "David"));
//        Collection<BookingEntity> initialBookings = new ArrayList<>(List.of(booking1, booking2));
//        bookingFileDao.save(initialBookings);
//
//        bookingFileDao.delete(ticketIdToDelete);
//
//        Collection<BookingEntity> result = bookingFileDao.getAll();
//        assertEquals(1, result.size());
//        assertFalse(result.contains(booking1));
//        assertTrue(result.contains(booking2));
//    }
//
//    @Test
//    void findOneBy() throws IOException {
//
//        long flightIdToFind = 1;
//        BookingEntity bookingToFind = new BookingEntity( flightIdToFind, List.of("Alice", "Bob"));
//        Predicate<BookingEntity> predicate = booking -> booking.getFlightId() == flightIdToFind;
//        Collection<BookingEntity> bookings = new ArrayList<>(List.of(bookingToFind));
//        bookingFileDao.save(bookings);
//
//        Optional<BookingEntity> result = bookingFileDao.findOneBy(predicate);
//
//        assertTrue(result.isPresent());
//        assertEquals(bookingToFind, result.get());
//    }
//
//    @Test
//    void findAllBy() throws IOException {
//
//        long flightIdToFind = 1;
//        BookingEntity booking1 = new BookingEntity( flightIdToFind, List.of("Alice", "Bob"));
//        BookingEntity booking2 = new BookingEntity( flightIdToFind, List.of("Charlie", "David"));
//        BookingEntity booking3 = new BookingEntity( flightIdToFind + 1, List.of("Emma", "Frank"));
//        Collection<BookingEntity> bookings = new ArrayList<>(List.of(booking1, booking2));
//        Predicate<BookingEntity> predicate = booking -> booking.getFlightId() == flightIdToFind;
//        bookingFileDao.save(bookings);
//
//        Collection<BookingEntity> result = bookingFileDao.findAllBy(predicate);
//
//        assertEquals(2, result.size());
//        assertTrue(result.contains(booking1));
//        assertTrue(result.contains(booking2));
//        assertFalse(result.contains(booking3));
//    }
//}
//
