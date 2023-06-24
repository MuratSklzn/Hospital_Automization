package model;

import java.sql.*;
import java.util.ArrayList;

public class Doctor extends User {
    Connection con = conn.connDb();
    Statement st = null;
    ResultSet rs = null;
    PreparedStatement preparedStatement = null;

    public Doctor() {
    }

    public Doctor(int id, String tcno, String name, String password, String type){
        super(id, tcno, name, password, type);
    }

    public boolean addWhour(int doctor_id, String doctor_name, String wdate) throws SQLException{
        boolean key = false;
        int count = 0;
        String query = "INSERT INTO whour" + "(doctor_id,doctor_name,wdate) VALUES" + "(?,?,?)";
        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM whour WHERE status='A' AND doctor_id = " + doctor_id + " AND wdate = '" + wdate + "'");
            while(rs.next()){
                count++;
                break;
            }
            if(count == 0) {
                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, doctor_id);
                preparedStatement.setString(2, doctor_name);
                preparedStatement.setString(3, wdate);
                preparedStatement.executeUpdate();
            }
            key = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return key;
    }
    public ArrayList<Whour> getWhourList(int doctor_id) {
        ArrayList<Whour> list = new ArrayList<>();
        Whour obj;
        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM whour WHERE status ='A' AND doctor_id = "+doctor_id);
            while (rs.next()) {
                obj = new Whour();
                obj.setId(rs.getInt("id"));
                obj.setDoctor_id(rs.getInt("doctor_id"));
                obj.setDoctor_name(rs.getString("doctor_name"));
                obj.setStatus("status");
                obj.setWdate(rs.getString("wdate"));
                list.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public ArrayList<Appointment> getAppointList(int doctor_id) {
        ArrayList<Appointment> list = new ArrayList<>();
        Appointment obj;
        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM appointment WHERE doctor_id = "+doctor_id);
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
    public boolean deleteWhour(int id) {
        String query = "DELETE FROM whour WHERE id = ?";
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
}
