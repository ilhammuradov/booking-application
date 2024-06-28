package az.edu.turing.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class FlightsDto {
    private LocalDateTime departureDateTime;
    private String destination;
    private String location;
    private int seats;
    private long flightId;

    public FlightsDto() {
    }

    public FlightsDto(long flightId, LocalDateTime departureDateTime, String destination, int seats) {
        this.flightId = flightId;
        this.departureDateTime = departureDateTime;
        this.destination = destination;
        this.seats = seats;
    }

    public FlightsDto(long flightId, LocalDateTime departureDateTime, String destination, String location, int seats) {
        this.flightId = flightId;
        this.departureDateTime = departureDateTime;
        this.destination = destination;
        this.location = location;
        this.seats = seats;
    }

    public LocalDateTime getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(LocalDateTime departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public long getFlightId() {
        return flightId;
    }

    public void setFlightId(long flightId) {
        this.flightId = flightId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlightsDto that = (FlightsDto) o;
        return seats == that.seats && flightId == that.flightId && Objects.equals(departureDateTime, that.departureDateTime) && Objects.equals(destination, that.destination) && Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departureDateTime, destination, location, seats, flightId);
    }

    @Override
    public String toString() {
        return "FlightsDto{" + "departureDateTime=" + departureDateTime + ", destination='" + destination + '\'' + ", location='" + location + '\'' + ", seats=" + seats + ", flightId=" + flightId + '}';
    }
}
