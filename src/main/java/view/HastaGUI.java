package view;

import helper.Helper;
import helper.Item;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class HastaGUI extends JFrame {
    private JPanel w_pane;
    private JButton bt_cikisYap;
    private JLabel lbl_hastaHg;
    private JTabbedPane w_tabPane;
    private JPanel w_appointment;
    private JComboBox cbo_selectClinic;
    private JTable tbl_doctor;
    private JScrollPane scp_scrollDoctor;
    private JButton bt_select;
    private JTable tbl_whour;
    private JScrollPane w_scrollWhour;
    private JButton bt_ap;
    private JPanel w_appoint;
    private JTable tbl_appoint;
    private JScrollPane w_scrollAppoint;
    private Clinic clinic = new Clinic();
    private DefaultTableModel doctorModel;
    private Object[] doctorData = null;
    private DefaultTableModel whourModel;
    private Object[] whourData = null;
    private DefaultTableModel appointModel;
    private Object[] appointData = null;
    private Whour whour = new Whour();
    private int selectedDoctorID = 0;
    private String selectedDoctorName = null;
    private Appointment appoint = new Appointment();
    private Hasta hasta;
    private JPopupMenu appointMenu;
    public HastaGUI(Hasta hasta) throws SQLException {
        this.hasta = hasta;
        lbl_hastaHg.setText("Hoşgeldiniz, Sayın " + hasta.getName());
        setContentPane(w_pane);
        setTitle("Hastane Yönetim Sistemi");
        setSize(750, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        doctorModel = new DefaultTableModel();
        Object[] colDoctor = new Object[2];
        colDoctor[0] = "ID";
        colDoctor[1] = "Ad Soyad";
        doctorModel.setColumnIdentifiers(colDoctor);
        doctorData = new Object[2];

        whourModel = new DefaultTableModel();
        Object[] colWhour = new Object[2];
        colWhour[0] = "ID";
        colWhour[1] = "Tarih";
        whourModel.setColumnIdentifiers(colWhour);
        whourData = new Object[2];

        cbo_selectClinic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cbo_selectClinic.getSelectedIndex() != 0) {
                    JComboBox c = (JComboBox) e.getSource();
                    Item item = (Item) c.getSelectedItem();
                    DefaultTableModel clearModel = (DefaultTableModel) tbl_doctor.getModel();
                    clearModel.setRowCount(0);

                    for(int i = 0; i < clinic.getClinicDoctorList(item.getKey()).size(); i++){
                        doctorData[0] = clinic.getClinicDoctorList(item.getKey()).get(i).getId();
                        doctorData[1] = clinic.getClinicDoctorList(item.getKey()).get(i).getName();
                        doctorModel.addRow(doctorData);
                    }
                    tbl_doctor.setModel(doctorModel);
                }else{
                    DefaultTableModel clearModel = (DefaultTableModel) tbl_doctor.getModel();
                    clearModel.setRowCount(0);
                }
            }
        });
        bt_select.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = tbl_doctor.getSelectedRow();
                if(row >= 0) {
                    String value = tbl_doctor.getModel().getValueAt(row,0).toString();
                    int id = Integer.parseInt(value);
                    DefaultTableModel clearModel = (DefaultTableModel) tbl_whour.getModel();
                    clearModel.setRowCount(0);

                    for(int i = 0; i < whour.getWhourList(id).size(); i++){
                        whourData[0] = whour.getWhourList(id).get(i).getId();
                        whourData[1] = whour.getWhourList(id).get(i).getWdate();
                        whourModel.addRow(whourData);
                    }
                    tbl_whour.setModel(whourModel);
                    selectedDoctorID = id;
                    selectedDoctorName = tbl_doctor.getModel().getValueAt(row, 1).toString();
                }else{
                    Helper.showMsg("Lütfen bir doktor seçiniz!");
                }
            }
        });
        bt_ap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selRow = tbl_whour.getSelectedRow();
                if(selRow >= 0){
                    String date = tbl_whour.getModel().getValueAt(selRow,1).toString();
                    try {
                        boolean control = hasta.addAppoinment(selectedDoctorID,hasta.getId(),selectedDoctorName,hasta.getName(),date);
                        if(control){
                            Helper.showMsg("success");
                            hasta.updateWhourStatus(selectedDoctorID, date);
                            updateWhourModel(selectedDoctorID);
                            updateAppointModel(hasta.getId());
                        }else{
                            Helper.showMsg("error");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }else{
                    Helper.showMsg("Lütfen uygun bir tarih seçiniz!");
                }
            }
        });
        bt_cikisYap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginGUI lGUI = new LoginGUI();
                lGUI.setVisible(true);
                dispose();
            }
        });
        appointData = new Object[3];
        for (int i = 0; i < appoint.getHastaList(hasta.getId()).size(); i++) {
            appointData[0] = appoint.getHastaList(hasta.getId()).get(i).getId();
            appointData[1] = appoint.getHastaList(hasta.getId()).get(i).getDoctorName();
            appointData[2] = appoint.getHastaList(hasta.getId()).get(i).getApDate();
            appointModel.addRow(appointData);
        }
    }


    public void updateWhourModel (int doctor_id){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_whour.getModel();
        clearModel.setRowCount(0);
        for(int i = 0; i < whour.getWhourList(doctor_id).size(); i++){
            whourData[0] = whour.getWhourList(doctor_id).get(i).getId();
            whourData[1] = whour.getWhourList(doctor_id).get(i).getWdate();
            whourModel.addRow(whourData);
        }
    }
    public void updateAppointModel (int hasta_id) throws SQLException {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_appoint.getModel();
        clearModel.setRowCount(0);
        for (int i = 0; i < appoint.getHastaList(hasta_id).size(); i++) {
            appointData[0] = appoint.getHastaList(hasta_id).get(i).getId();
            appointData[1] = appoint.getHastaList(hasta_id).get(i).getDoctorName();
            appointData[2] = appoint.getHastaList(hasta_id).get(i).getApDate();
            appointModel.addRow(appointData);
        }
    }
    private void createUIComponents() throws SQLException {

            Clinic clinic = new Clinic();
            cbo_selectClinic = new JComboBox<>();
            cbo_selectClinic.addItem("Poliklinik seç.");
            for (int i = 0; i < clinic.getList().size(); i++) {
                cbo_selectClinic.addItem(new Item(clinic.getList().get(i).getId(), clinic.getList().get(i).getName()));
            }

            Appointment appoint = new Appointment();
            appointModel = new DefaultTableModel();
            Object[] colAppoint = new Object[3];
            colAppoint[0] = "ID";
            colAppoint[1] = "Doktor";
            colAppoint[2] = "Tarih";
            appointModel.setColumnIdentifiers(colAppoint);
            tbl_appoint = new JTable(appointModel);

        appointMenu = new JPopupMenu();
        JMenuItem deleteMenu = new JMenuItem("Sil");
        appointMenu.add(deleteMenu);
        tbl_appoint.setComponentPopupMenu(appointMenu);
        deleteMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Helper.confirm("sure")) {
                    int selRow = tbl_appoint.getSelectedRow();
                    String doctor_name = tbl_appoint.getModel().getValueAt(selRow,1).toString();
                    String date = tbl_appoint.getModel().getValueAt(selRow,2).toString();
                    int selectedID = Integer.parseInt(tbl_appoint.getValueAt(tbl_appoint.getSelectedRow(), 0).toString());
                    try {
                        if (hasta.deleteAppoint(selectedID) && hasta.activateWhourStatus(doctor_name,date)) {
                            Helper.showMsg("success");
                            try {
                                updateAppointModel(hasta.getId());
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            Helper.showMsg("error");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        }


    public static void main(String[] args) throws SQLException {
        Hasta hasta = new Hasta();
        HastaGUI hGUI = new HastaGUI(hasta);
        hGUI.setVisible(true);

    }

}
