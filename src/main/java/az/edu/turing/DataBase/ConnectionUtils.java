package az.edu.turing.DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtils {

    public static Connection getConnection(DBProperties db) {
        Connection con = null;
        try {
            con = DriverManager.getConnection(db.url, db.user, db.password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return con;
    }
}
