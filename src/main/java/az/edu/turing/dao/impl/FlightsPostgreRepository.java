package az.edu.turing.dao.impl;

import az.edu.turing.DataBase.ConnectionUtils;
import az.edu.turing.DataBase.DBProperties;
import az.edu.turing.dao.FlightsRepository;
import az.edu.turing.dao.entity.FlightsEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class FlightsPostgreRepository extends FlightsRepository {

    private final String url = "jdbc:postgresql://localhost:5432/Booking-Application";
    private final String user = "postgres";
    private final String password = "1643";
    DBProperties db;

    public FlightsPostgreRepository() {
        this.db = new DBProperties(url, user, password);
    }

    @Override
    public boolean save(Collection<FlightsEntity> flights) {
        String sql = "INSERT INTO flights(location, destination, departure_time, free_seats) VALUES(?,?,?,?)";
        Connection con = null;
        try {
            con = ConnectionUtils.getConnection(db);
            con.setAutoCommit(false);
            PreparedStatement pst = con.prepareStatement(sql);

            for (FlightsEntity flight : flights) {
                pst.setString(1, flight.getLocation());
                pst.setString(2, flight.getDestination());
                pst.setTimestamp(3, Timestamp.valueOf(flight.getDepartureDateTime()));
                pst.setInt(4, flight.getSeats());
                pst.addBatch();
            }

            int[] affectedRows = pst.executeBatch();

            con.commit();
            return affectedRows.length == flights.size();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public Collection<FlightsEntity> getAll() {
        String sql = "SELECT * FROM flights";
        Collection<FlightsEntity> flights = new ArrayList<>();

        try (Connection con = ConnectionUtils.getConnection(db); PreparedStatement pst = con.prepareStatement(sql)) {

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                FlightsEntity flight = new FlightsEntity();
                flight.setFlightId(rs.getLong("id"));
                flight.setLocation(rs.getString("location"));
                flight.setDestination(rs.getString("destination"));
                flight.setDepartureDateTime(rs.getTimestamp("departure_time").toLocalDateTime());
                flight.setSeats(rs.getInt("free_seats"));

                flights.add(flight);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return flights;
    }


    @Override
    public void delete(long flightId) {
        String sql = "DELETE FROM flights WHERE id = ?";

        try (Connection con = ConnectionUtils.getConnection(db); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setLong(1, flightId);
            int affectedRows = pst.executeUpdate();

            if (affectedRows == 0) {
                System.out.println("No flight found with id: " + flightId);
            } else {
                System.out.println("Flight with id: " + flightId + " was deleted successfully.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(FlightsEntity flight) {
        String sql = "UPDATE flights SET location = ?, destination = ?, departure_time = ?, free_seats = ? WHERE id = ?";

        try (Connection con = ConnectionUtils.getConnection(db); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, flight.getLocation());
            pst.setString(2, flight.getDestination());
            pst.setTimestamp(3, Timestamp.valueOf(flight.getDepartureDateTime()));
            pst.setInt(4, flight.getSeats());
            pst.setLong(5, flight.getFlightId());

            int affectedRows = pst.executeUpdate();

            if (affectedRows == 0) {
                System.out.println("No flight found with id: " + flight.getFlightId());
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<FlightsEntity> findOneBy(Predicate<FlightsEntity> predicate) {
        return getAll().stream().filter(predicate).findFirst();
    }

    @Override
    public List<FlightsEntity> findAllBy(Predicate<FlightsEntity> predicate) {
        return getAll().stream().filter(predicate).toList();
    }
}
