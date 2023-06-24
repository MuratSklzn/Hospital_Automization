package model;


import helper.Helper;

import java.sql.*;
import java.util.ArrayList;

public class Bashekim extends User {
    Connection con = conn.connDb();
    Statement st = null;
    ResultSet rs = null;
    PreparedStatement preparedStatement = null;

    public Bashekim(int id, String tcno, String name, String password, String type) {
        super(id, tcno, name, password, type);
    }

    public Bashekim() {
    }

    public ArrayList<User> getDoctorList() {
        ArrayList<User> list = new ArrayList<>();
        User obj;
        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM user WHERE type= 'doktor'");
            while (rs.next()) {
                obj = new Doctor(rs.getInt("id"), rs.getString("tcno"), rs.getString("name"), rs.getString("password"), rs.getString("type"));
                list.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
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
    
    //overloading
    public boolean add(String tcno, String password, String name) {
        String query = "INSERT INTO user" + "(tcno,password,name,type) VALUES" + "(?,?,?,?)";
        boolean key = false;
        try {
            st = con.createStatement();
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, tcno);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, "doktor");
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
    public boolean add(int user_id, int clinic_id) {
        String query = "INSERT INTO employee" + "(user_id , clinic_id) VALUES" + "(?,?)";
        boolean key = false;
        int count =0;
        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM employee WHERE clinic_id="+ clinic_id + " AND user_id=" + user_id);
            while(rs.next()){
                count++;
            }
            if ( count == 0) {
                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, user_id);
                preparedStatement.setInt(2, clinic_id);
                preparedStatement.executeUpdate();
            }
            else{
                throw new NullPointerException("Kullanıcı zaten mevcut.");
            }
            key = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return key;
    }

    public boolean deleteDoctor(int id) {
        String query = "DELETE FROM user WHERE id = ?";
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
    public boolean updateDoctor(int id, String tcno, String password, String name) {
        String query = "UPDATE user SET name = ?, tcno=?, password=? WHERE id = ?";
        boolean key = false;
        try {
            st = con.createStatement();
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, tcno);
            preparedStatement.setString(3, password);
            preparedStatement.setInt(4, id);
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


    public ArrayList<Appointment> getAppointList() {
        ArrayList<Appointment> list = new ArrayList<>();
        Appointment obj;
        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM appointment");
            while (rs.next()) {
                obj = new Appointment();
                obj.setId(rs.getInt("id"));
                obj.setDoctorId(rs.getInt("doctor_id"));
                obj.setDoctorName(rs.getString("doctor_name"));
                obj.setHastaId(rs.getInt("hasta_id"));
                obj.setHastaName(rs.getString("hasta_name"));
                obj.setApDate(rs.getString("ap_date"));
                list.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
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
