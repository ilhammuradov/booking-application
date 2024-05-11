package az.edu.turing.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public final class FlightsEntity {
    private LocalDateTime departureDateTime;
    private String destination;
    private String location;
    private int seats;
    private long flightId;
    private static long MAX_ID = 0;

    public FlightsEntity() {
    }

    public FlightsEntity(LocalDateTime departureDateTime, String destination, int seats) {
        this.flightId = ++MAX_ID;
        this.departureDateTime = departureDateTime;
        this.destination = destination;
        this.seats = seats;
    }

    public FlightsEntity(long flightId, LocalDateTime departureDateTime, String destination, String location, int seats) {
        this.flightId = flightId;
        this.departureDateTime = departureDateTime;
        this.destination = destination;
        this.location = location;
        this.seats = seats;
    }

    public LocalDateTime getDepartureDateTime() {
        return departureDateTime;
    }

    public String getDestination() {
        return destination;
    }

    public String getLocation() {
        return location;
    }

    public int getSeats() {
        return seats;
    }

    public long getFlightId() {
        return flightId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlightsEntity that = (FlightsEntity) o;
        return seats == that.seats && flightId == that.flightId && Objects.equals(departureDateTime, that.departureDateTime) && Objects.equals(destination, that.destination) && Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departureDateTime, destination, location, seats, flightId);
    }

    @Override
    public String toString() {
        return "FlightsEntity{" +
                "departureDateTime=" + departureDateTime +
                ", destination='" + destination + '\'' +
                ", location='" + location + '\'' +
                ", seats=" + seats +
                ", flightId=" + flightId +
                '}';
    }
}
