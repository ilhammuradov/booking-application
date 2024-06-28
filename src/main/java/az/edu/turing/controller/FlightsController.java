package az.edu.turing.controller;

import az.edu.turing.dao.entity.FlightsEntity;
import az.edu.turing.model.FlightsDto;
import az.edu.turing.service.FlightsService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public class FlightsController {
    private final FlightsService flightService;

    public FlightsController(FlightsService flightService) {
        this.flightService = flightService;
    }

    public void createFlights(FlightsDto flightsDto) {
        flightService.createFlights(flightsDto);
    }

    public Collection<FlightsDto> getAllFlights() {
        return flightService.getAllFlights();
    }

    public Collection<FlightsEntity> getAllFlightsByDestination(String destination) {
        return flightService.getAllFlightsByDestination(destination);
    }

    public Collection<FlightsEntity> getAllFlightsByLocation(String location) {
        return flightService.getAllFlightsByLocation(location);
    }

    public Optional<FlightsEntity> getOneFlightByFlightId(long flightId) {
        return flightService.getOneFlightByFlightId(flightId);
    }

    public Collection<FlightsDto> getOnlineBoard(String location, LocalDateTime dateTime) {
        return flightService.flightsInNext24Hours(location, dateTime);
    }
}