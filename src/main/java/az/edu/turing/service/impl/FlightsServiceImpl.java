package az.edu.turing.service.impl;

import az.edu.turing.dao.FlightsRepository;
import az.edu.turing.dao.entity.FlightsEntity;
import az.edu.turing.model.FlightsDto;
import az.edu.turing.service.FlightsService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

public class FlightsServiceImpl implements FlightsService {

    private final FlightsRepository flightsRepository;

    public FlightsServiceImpl(FlightsRepository flightsRepository) {
        this.flightsRepository = flightsRepository;
    }

    @Override
    public void createFlights(FlightsDto flightsDto) {
        FlightsEntity flightsEntity = new FlightsEntity(flightsDto.getDepartureDateTime(), flightsDto.getDestination(), flightsDto.getLocation(), flightsDto.getSeats(), flightsDto.getFlightId());
        ArrayList<FlightsEntity> flightForAdd = new ArrayList<>();
        flightForAdd.add(flightsEntity);
        flightsRepository.save(flightForAdd);
    }

    @Override
    public Collection<FlightsDto> getAllFlights() {
        Collection<FlightsEntity> flights = flightsRepository.getAll();
        ArrayList<FlightsDto> flightsDto = new ArrayList<>();
        flights.stream().forEach(flightsEntity -> flightsDto.add(new FlightsDto(flightsEntity.getFlightId(), flightsEntity.getDepartureDateTime(), flightsEntity.getDestination(), flightsEntity.getLocation(), flightsEntity.getSeats())));
        return flightsDto;
    }

    @Override
    public Collection<FlightsEntity> getAllFlightsByDestination(String destination) {
        Predicate<FlightsEntity> predicate = flightsEntity -> flightsEntity.getDestination().equalsIgnoreCase(destination);
        return flightsRepository.findAllBy(predicate);
    }

    @Override
    public Collection<FlightsEntity> getAllFlightsByLocation(String location) {
        Predicate<FlightsEntity> predicate = flightsEntity -> flightsEntity.getLocation().equalsIgnoreCase(location);
        return flightsRepository.findAllBy(predicate);
    }


    @Override
    public Optional<FlightsEntity> getOneFlightByFlightId(long flightId) {
        Predicate<FlightsEntity> predicate = flightsEntity -> flightsEntity.getFlightId() == flightId;
        return flightsRepository.findOneBy(predicate);
    }

    @Override
    public Collection<FlightsDto> flightsInNext24Hours(String location, LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime next24Hours = now.plusHours(24);
        Collection<FlightsDto> allFlights = getAllFlights();
        return allFlights.stream().filter(flightsDto -> flightsDto.getLocation().equalsIgnoreCase(location) && flightsDto.getDepartureDateTime().isAfter(now) && flightsDto.getDepartureDateTime().isBefore(next24Hours)).toList();
    }
}
