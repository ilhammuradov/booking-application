package az.edu.turing.service;

import az.edu.turing.dao.entity.FlightsEntity;
import az.edu.turing.model.FlightsDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface FlightsService {
    void createFlights(FlightsDto flightsDto);

    Collection<FlightsDto> getAllFlights();

    Collection<FlightsEntity> getAllFlightsByDestination(String destination);

    Collection<FlightsEntity> getAllFlightsByLocation(String location);

    Optional<FlightsEntity> getOneFlightByFlightId(long flightId);

    Collection<FlightsDto> flightsInNext24Hours(String location, LocalDateTime dateTime);
}
