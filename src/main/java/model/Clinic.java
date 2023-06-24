package model;

import helper.DBConnection;

import java.sql.*;
import java.util.ArrayList;

public class Clinic {

    private int id;
    private String name;
    DBConnection conn = new DBConnection();
    Connection con = conn.connDb();
    Statement st = null;
    ResultSet rs = null;
    PreparedStatement preparedStatement = null;

    public Clinic() {
    }

    public Clinic(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public ArrayList<Clinic> getList() throws SQLException {
        ArrayList<Clinic> list = new ArrayList<>();
        Clinic obj;
        Connection con = conn.connDb();
        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM clinic");
            while (rs.next()) {
                obj = new Clinic(rs.getInt("id"), rs.getString("name"));
                list.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            st.close();
            rs.close();
            con.close();
        }

        return list;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public boolean addClinic(String name) {
        String query = "INSERT INTO clinic" + "(name) VALUES" + "(?)";
        boolean key = false;
        try {
            st = con.createStatement();
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, name);
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
    public boolean deleteClinic(int id) {
        String query = "DELETE FROM clinic WHERE id = ?";
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
    public boolean updateClinic(int id,String name) {
        String query = "UPDATE clinic SET name = ? WHERE id = ?";
        boolean key = false;
        try {
            st = con.createStatement();
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, id);
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
    public ArrayList<User> getClinicDoctorList(int clinic_id) {
        ArrayList<User> list = new ArrayList<>();
        User obj;
        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT u.id, u.tcno, u.type, u.name, u.password FROM employee e LEFT JOIN user u ON e.user_id = u.id WHERE clinic_id = " + clinic_id);
            while (rs.next()) {
                obj = new Doctor(rs.getInt("u.id"), rs.getString("u.tcno"), rs.getString("u.name"), rs.getString("u.password"), rs.getString("u.type"));
                list.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Clinic getFetch(int id) throws SQLException {
        Clinic c = new Clinic();
        Connection con = conn.connDb();
        st = con.createStatement();
        rs = st.executeQuery("SELECT * FROM clinic WHERE id=" + id );
        while (rs.next()){
            c.setId(rs.getInt("id"));
            c.setName(rs.getString("name"));
        }
        return c;
    }

}
