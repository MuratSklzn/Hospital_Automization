package helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    Connection c = null;

    public DBConnection() {}

    public Connection connDb() {

        try {
            String url = "jdbc:mariadb://localhost:3306/hospital";
            String user = "root";
            String password = "1238";
            this.c = DriverManager.getConnection(url,user,password);
            return c;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }
}

