package model;

import helper.DBConnection;

import java.sql.*;
import java.util.ArrayList;

public class Appointment {
    private int id, doctorId, hastaId;
    private String doctorName, hastaName,apDate;
    DBConnection conn = new DBConnection();
    Connection con = conn.connDb();
    Statement st = null;
    ResultSet rs = null;
    PreparedStatement preparedStatement = null;

    public Appointment() {
    }

    public Appointment(int id, int doctorId, int hastaId, String doctorName, String hastaName, String apDate) {
        this.id = id;
        this.doctorId = doctorId;
        this.hastaId = hastaId;
        this.doctorName = doctorName;
        this.hastaName = hastaName;
        this.apDate = apDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getHastaId() {
        return hastaId;
    }

    public void setHastaId(int hastaId) {
        this.hastaId = hastaId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getHastaName() {
        return hastaName;
    }

    public void setHastaName(String hastaName) {
        this.hastaName = hastaName;
    }

    public String getApDate() {
        return apDate;
    }

    public void setApDate(String apDate) {
        this.apDate = apDate;
    }
    public ArrayList<Appointment> getHastaList(int hasta_id) throws SQLException {
        ArrayList<Appointment> list = new ArrayList<>();
        Appointment obj;
        Connection con = conn.connDb();
        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM appointment WHERE hasta_id = " + hasta_id);
            while (rs.next()) {
                obj = new Appointment();
                obj.setId(rs.getInt("id"));
                obj.setDoctorId(rs.getInt("doctor_id"));
                obj.setHastaId(rs.getInt("hasta_id"));
                obj.setDoctorName(rs.getString("doctor_name"));
                obj.setHastaName(rs.getString("hasta_name"));
                obj.setApDate(rs.getString("ap_date"));
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

}
