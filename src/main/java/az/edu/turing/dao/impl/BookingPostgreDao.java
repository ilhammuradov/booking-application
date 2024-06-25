package az.edu.turing.dao.impl;

import az.edu.turing.DataBase.ConnectionUtils;
import az.edu.turing.DataBase.DBProperties;
import az.edu.turing.dao.BookingDao;
import az.edu.turing.entity.BookingEntity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class BookingPostgreDao extends BookingDao {

    private final String url="jdbc:postgresql://localhost:5432/Booking-Application";
    private final String user="postgres";
    private final String password="1643";
    DBProperties db;
    public BookingPostgreDao() {
        this.db=new DBProperties(url,user,password);
    }
    @Override
    public boolean save(Collection<BookingEntity> t) {
        String sql="insert into bookings(flight_ID) values(?)";
        String sql1="insert into bookings_passengers(full_name,booking_ID) values(?,?)";
        Connection conn=null;
        try {
            conn = ConnectionUtils.getConnection(db);
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement(sql);
            PreparedStatement ps1 = conn.prepareStatement(sql1);
            for (BookingEntity booking : t) {
                ps.setLong(1, booking.getFlightId());
                ps.addBatch();

                List<String> passengerNames = booking.getPassengerNames();
                for (String fullName : passengerNames) {
                    ps1.setString(1, fullName);
                    ps1.setLong(2, booking.getTicketId());
                    ps1.executeUpdate();
                }
            }

            conn.commit();
            return true;
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
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Collection<BookingEntity> getAll() {
        return List.of();
    }

    @Override
    public void delete(long flightId) {

    }

    @Override
    public Optional<BookingEntity> findOneBy(Predicate<BookingEntity> predicate) {
        return Optional.empty();
    }

    @Override
    public Collection<BookingEntity> findAllBy(Predicate<BookingEntity> predicate) {
        return List.of();
    }
}
