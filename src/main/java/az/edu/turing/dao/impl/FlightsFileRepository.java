package az.edu.turing.dao.impl;

import az.edu.turing.dao.FlightsRepository;
import az.edu.turing.dao.entity.FlightsEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

public class FlightsFileRepository extends FlightsRepository {
    private static final String RESOURCE_PATH = "src/main/java/az/edu/turing/resource/";
    private static final String FLIGHTS_FILE_PATH = RESOURCE_PATH.concat("flights.json");
    private final ObjectMapper objectMapper;

    public FlightsFileRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean save(Collection<FlightsEntity> flights) {
        try {
            FileWriter fw = new FileWriter(FLIGHTS_FILE_PATH);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(objectMapper.writeValueAsString(flights));
            bw.close();
            return true;
        } catch (IOException e) {
            System.err.println("Error while adding new flight: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Collection<FlightsEntity> getAll() {
        try {
            FileReader fr = new FileReader(FLIGHTS_FILE_PATH);
            BufferedReader x = new BufferedReader(fr);
            String jsonData = x.readLine();
            if (jsonData != null && !jsonData.isBlank()) {
                FlightsEntity[] flights = objectMapper.readValue(jsonData, FlightsEntity[].class);
                x.close();
                return Arrays.stream(flights).toList();
            }
            x.close();
        } catch (IOException e) {
            System.out.println("Error while reading flights from file: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public void delete(long flightId) {
        Collection<FlightsEntity> bookings = new ArrayList<>(getAll());
        bookings.removeIf(flights -> flights.getFlightId() == flightId);
        save(bookings);
    }

    @Override
    public Optional<FlightsEntity> findOneBy(Predicate<FlightsEntity> predicate) {
        return getAll().stream().filter(predicate).findFirst();
    }

    @Override
    public Collection<FlightsEntity> findAllBy(Predicate<FlightsEntity> predicate) {
        return getAll().stream().filter(predicate).toList();
    }

    @Override
    public void update(FlightsEntity flightsEntity) {
    }
}
