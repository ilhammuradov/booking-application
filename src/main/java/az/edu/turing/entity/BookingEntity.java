package az.edu.turing.entity;

import java.io.*;
import java.util.List;
import java.util.Objects;

public final class BookingEntity {

    private static long MAX_ID = loadMaxId();
    private long ticketId;
    private long flightId;
    private List<String> passengerNames;

    public BookingEntity() {
    }

    public BookingEntity(long flightId, List<String> passengerNames) {
        this.ticketId = ++MAX_ID;
        this.flightId = flightId;
        this.passengerNames = passengerNames;
        saveMaxId(MAX_ID);
    }

    public long getTicketId() {
        return ticketId;
    }

    public void setTicketId(long ticketId) {
        this.ticketId = ticketId;
    }

    public long getFlightId() {
        return flightId;
    }

    public void setFlightId(long flightId) {
        this.flightId = flightId;
    }

    public List<String> getPassengerNames() {
        return passengerNames;
    }

    public void setPassengerNames(List<String> passengerNames) {
        this.passengerNames = passengerNames;
    }

    @Override
    public String toString() {
        return "{ticketId=%d, flightId=%d, passengerNames=%s}".formatted(ticketId, flightId, passengerNames);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingEntity that = (BookingEntity) o;
        return ticketId == that.ticketId && flightId == that.flightId && Objects.equals(passengerNames, that.passengerNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketId, flightId, passengerNames);
    }

    private static long loadMaxId() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/az/edu/turing/resource/max_id.txt"))) {
            return Long.parseLong(reader.readLine());
        } catch (IOException | NumberFormatException e) {
            return 0;
        }
    }

    private static void saveMaxId(long maxId) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/az/edu/turing/resource/max_id.txt"))) {
            writer.write(Long.toString(maxId));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
