package az.edu.turing.dao.impl;

import az.edu.turing.DataBase.ConnectionUtils;
import az.edu.turing.DataBase.DBProperties;
import az.edu.turing.dao.BookingRepository;
import az.edu.turing.dao.entity.BookingEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Predicate;

public class BookingPostgresRepository extends BookingRepository {

    private final String url = "jdbc:postgresql://localhost:5430/Booking-Application";
    private final String user = "postgres";
    private final String password = "postgres";
    DBProperties db;

    public BookingPostgresRepository() {
        this.db = new DBProperties(url, user, password);
    }

    @Override
    public boolean save(Collection<BookingEntity> bookings) {
        String insertBookingSQL = "INSERT INTO bookings (flight_id) VALUES (?) RETURNING id";
        String insertPassengerSQL = "INSERT INTO bookings_passengers (booking_id, full_name) VALUES (?, ?)";

        Connection con = null;
        try {
            con = ConnectionUtils.getConnection(db);
            con.setAutoCommit(false);

            try (PreparedStatement insertBookingPst = con.prepareStatement(insertBookingSQL); PreparedStatement insertPassengerPst = con.prepareStatement(insertPassengerSQL)) {

                for (BookingEntity booking : bookings) {
                    insertBookingPst.setLong(1, booking.getFlightId());
                    ResultSet rs = insertBookingPst.executeQuery();

                    if (rs.next()) {
                        long bookingId = rs.getLong(1);

                        for (String passengerName : booking.getPassengerNames()) {
                            insertPassengerPst.setLong(1, bookingId);
                            insertPassengerPst.setString(2, passengerName.toUpperCase());
                            insertPassengerPst.addBatch();
                        }
                    }
                }

                insertPassengerPst.executeBatch();
                con.commit();
                return true;
            } catch (SQLException e) {
                if (con != null) {
                    con.rollback();
                }
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public Collection<BookingEntity> getAll() {
        String sql = "select id, flight_id, full_name " + "from bookings " + "inner join bookings_passengers on bookings.id=bookings_passengers.booking_id";
        Collection<BookingEntity> bookings = new ArrayList<>();

        try (Connection con = ConnectionUtils.getConnection(db); PreparedStatement pst = con.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {

            Map<Long, BookingEntity> bookingEntityMap = new HashMap<>();
            while (rs.next()) {
                long bookingID = rs.getLong("id");
                long flightID = rs.getLong("flight_id");
                String fullname = rs.getString("full_name");
                BookingEntity booking = bookingEntityMap.get(bookingID);

                if (booking == null) {
                    booking = new BookingEntity();
                    booking.setTicketId(bookingID);
                    booking.setFlightId(flightID);
                    booking.setPassengerNames(new ArrayList<>());
                    bookingEntityMap.put(bookingID, booking);
                }
                booking.getPassengerNames().add(fullname);
            }

            bookings.addAll(bookingEntityMap.values());

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    @Override
    public void delete(long bookingId) {
        String sql1 = "DELETE FROM bookings WHERE id = ?";
        String sql2 = "DELETE FROM bookings_passengers WHERE booking_id = ?";
        Connection conn = null;
        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;

        try {
            conn = ConnectionUtils.getConnection(db);
            conn.setAutoCommit(false);

            pst1 = conn.prepareStatement(sql1);
            pst2 = conn.prepareStatement(sql2);

            pst1.setLong(1, bookingId);
            pst2.setLong(1, bookingId);

            pst2.executeUpdate();
            int affectedRows = pst1.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                System.out.println("No such booking with booking id: " + bookingId);
            } else {
                conn.commit();
                System.out.println("Booking with id: " + bookingId + " was deleted successfully.");
            }

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if (pst1 != null) pst1.close();
                if (pst2 != null) pst2.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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
