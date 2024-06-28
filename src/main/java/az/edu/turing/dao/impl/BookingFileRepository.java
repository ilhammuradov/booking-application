package az.edu.turing.dao.impl;

import az.edu.turing.dao.BookingRepository;
import az.edu.turing.dao.entity.BookingEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

public class BookingFileRepository extends BookingRepository {

    private static final String RESOURCE_PATH = "src/main/java/az/edu/turing/resource/";
    private static final String BOOKINGS_FILE_PATH = RESOURCE_PATH.concat("bookings.bean");
    private final ObjectMapper objectMapper;

    public BookingFileRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean save(Collection<BookingEntity> bookings) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(BOOKINGS_FILE_PATH));
            bw.write(objectMapper.writeValueAsString(bookings));
            bw.close();
            return true;
        } catch (IOException e) {
            System.err.println("Error while adding new booking: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Collection<BookingEntity> getAll() {
        try {
            BufferedReader x = new BufferedReader(new FileReader(BOOKINGS_FILE_PATH));
            String jsonData = x.readLine();
            if (jsonData != null && !jsonData.isBlank()) {
                BookingEntity[] bookings = objectMapper.readValue(jsonData, BookingEntity[].class);
                x.close();
                return Arrays.stream(bookings).toList();
            }
            x.close();
        } catch (IOException e) {
            System.out.println("Error while reading bookings from file: " + e.getMessage());
        }
        return null;
    }


    @Override
    public void delete(long ticketId) {
        Collection<BookingEntity> bookings = new ArrayList<>(getAll());
        bookings.removeIf(booking -> booking.getTicketId() == ticketId);
        save(bookings);
    }


    @Override
    public Optional<BookingEntity> findOneBy(Predicate<BookingEntity> predicate) {
        return getAll().stream().filter(predicate).findFirst();
    }

    @Override
    public Collection<BookingEntity> findAllBy(Predicate<BookingEntity> predicate) {
        return getAll().stream().filter(predicate).toList();
    }
}
