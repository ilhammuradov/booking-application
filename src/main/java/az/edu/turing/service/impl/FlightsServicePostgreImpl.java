package az.edu.turing.service.impl;

import az.edu.turing.model.FlightsDto;
import az.edu.turing.service.FlightsService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class FlightsServicePostgreImpl implements FlightsService {
    @Override
    public void createFlights(FlightsDto flightsDto) {

    }

    @Override
    public Collection<FlightsDto> getAllFlights() {
        return List.of();
    }

    @Override
    public List<FlightsDto> getAllFlightsByDestination(String destination) {
        return List.of();
    }

    @Override
    public List<FlightsDto> getAllFlightsByLocation(String location) {
        return List.of();
    }

    @Override
    public List<FlightsDto> getFlightInfoByFlightId(long flightId) {
        return List.of();
    }

    @Override
    public Optional<FlightsDto> getOneFlightByFlightId(long flightId) {
        return Optional.empty();
    }

    @Override
    public List<FlightsDto> flightsInNext24Hours(String location, LocalDateTime dateTime) {
        return List.of();
    }
}
