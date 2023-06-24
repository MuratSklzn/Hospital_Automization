package model;

import helper.Helper;

import java.sql.*;

public class Hasta extends User {
    Connection con = conn.connDb();
    Statement st = null;
    ResultSet rs = null;
    PreparedStatement preparedStatement = null;

    public Hasta() {
    }

    public Hasta(int id, String tcno, String name, String password, String type) {
        super(id, tcno, name, password, type);
    }

    public boolean register(String tcno, String password, String name) throws SQLException {
        boolean key = false;
        boolean duplicate = false;
        String query = "INSERT INTO user" + "(tcno,password,name,type) VALUES" + "(?,?,?,?)";
        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM user WHERE tcno = '" + tcno + "'");
            while (rs.next()) {
                duplicate = true;
                break;
            }
            if (!duplicate) {
                preparedStatement = con.prepareStatement(query);
                preparedStatement.setString(1, tcno);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, name);
                preparedStatement.setString(4, "hasta");
                preparedStatement.executeUpdate();
                key = true;
            } else {
                Helper.showMsg("Bu TC no ile kayıtlı kullanıcı bulunmaktadır.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return key;
    }

    public boolean addAppoinment(int doctor_id, int hasta_id, String doctor_name, String hasta_name, String ap_date) throws SQLException {
        boolean key = false;
        String query = "INSERT INTO appointment" + "(doctor_id,doctor_name,hasta_id,hasta_name,ap_date) VALUES" + "(?,?,?,?,?)";
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, doctor_id);
            preparedStatement.setString(2, doctor_name);
            preparedStatement.setInt(3, hasta_id);
            preparedStatement.setString(4, hasta_name);
            preparedStatement.setString(5, ap_date);
            preparedStatement.executeUpdate();
            key = true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return key;
    }
    public boolean deleteAppoint(int id) {
        String query = "DELETE FROM appointment WHERE id = ?";
        boolean key = false;
        try {
            st = con.createStatement();
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            key = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (key)
            return true;
        else
            return false;
    }
    public boolean updateWhourStatus(int doctor_id,String wdate) throws SQLException {
        boolean key = false;
        String query = "UPDATE whour SET status = ? WHERE doctor_id = ? AND wdate = ?";
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, "P");
            preparedStatement.setInt(2, doctor_id);
            preparedStatement.setString(3, wdate);
            preparedStatement.executeUpdate();
            key = true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return key;
    }
    public boolean activateWhourStatus(String doctor_name,String wdate) throws SQLException {
        boolean key = false;
        String query = "UPDATE whour SET status = ? WHERE doctor_name = ? AND wdate = ?";
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, "A");
            preparedStatement.setString(2, doctor_name);
            preparedStatement.setString(3, wdate);
            preparedStatement.executeUpdate();
            key = true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return key;
    }

}
