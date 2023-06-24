package view;

import com.toedter.calendar.JDateChooser;
import helper.Helper;
import model.Doctor;
import model.Hasta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DoctorGUI extends JFrame {
    private JPanel w_pane;
    private JLabel lbl_doctorHg;
    private JButton bt_cikisYap;
    private JTabbedPane w_tab;
    private JPanel w_whour;
    private JDateChooser dc_date;
    private JComboBox cbo_time;
    private JButton bt_addwhour;
    private JTable tbl_whour;
    private JScrollPane w_scroll;
    private JButton bt_deleteWhour;
    private JTable tbl_appoint;
    private JScrollPane w_appoint;
    private JPanel panel1;
    private DefaultTableModel appointModel;
    private Object[] appointData = null;
    private DefaultTableModel whourModel;
    private Object[] whourData = null;
    private Doctor doctor;
    private JPopupMenu appointMenu;
    private Hasta hasta = new Hasta();

    public DoctorGUI(Doctor doctor) {
        this.doctor = doctor;
        lbl_doctorHg.setText("Hoşgeldiniz, Sayın " + doctor.getName());
        setContentPane(w_pane);
        setTitle("Hastane Yönetim Sistemi");
        setSize(750, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        bt_addwhour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String date = "";
                try {
                    date = sdf.format(dc_date.getDate());
                } catch (Exception ex) {
                }

                if (date.length() == 0) {
                    Helper.showMsg("Lütfen bir tarih seçiniz!");
                } else {
                    String time = " " + cbo_time.getSelectedItem().toString() + ":00";
                    String selectDate = date + time;
                    try {
                        boolean control = doctor.addWhour(doctor.getId(), doctor.getName(), selectDate);
                        if (control) {
                            ArrayList list = new ArrayList<>();
                            for (int i = 0; i < tbl_whour.getModel().getRowCount(); i++) {
                                list.add(tbl_whour.getModel().getValueAt(i, 1).toString());
                                if (list.get(i).equals(selectDate)) {
                                    Helper.showMsg("Randevu mecvut!");
                                    return;
                                }
                            }
                            Helper.showMsg("success");
                            updateWhourModel(doctor);
                        } else {
                            Helper.showMsg("error");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                }
            }
        });
        bt_deleteWhour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selRow = tbl_whour.getSelectedRow();
                if (selRow >= 0) {
                    String selectRow = tbl_whour.getModel().getValueAt(selRow, 0).toString();
                    int selID = Integer.parseInt(selectRow);
                    boolean control = doctor.deleteWhour(selID);
                    if (control) {
                        Helper.showMsg("success");
                        updateWhourModel(doctor);
                    } else {
                        Helper.showMsg("error");
                    }
                } else {
                    Helper.showMsg("Lütfen bir randevu seçiniz.");
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
        appointMenu = new JPopupMenu();
        JMenuItem deleteMenu = new JMenuItem("Sil");
        appointMenu.add(deleteMenu);
        tbl_appoint.setComponentPopupMenu(appointMenu);
        deleteMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Helper.confirm("sure")) {
                    int selRow = tbl_appoint.getSelectedRow();
                    String date = tbl_appoint.getModel().getValueAt(selRow,2).toString();
                    int selectedID = Integer.parseInt(tbl_appoint.getValueAt(tbl_appoint.getSelectedRow(), 0).toString());
                    try {
                        if (doctor.deleteAppoint(selectedID) && doctor.activateWhourStatus(doctor.getName(),date)) {
                            Helper.showMsg("success");
                            updateAppointModel(doctor);
                            updateWhourModel(doctor);
                        } else {
                            Helper.showMsg("error");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        appointData = new Object[3];
        for (int i = 0; i < doctor.getAppointList(doctor.getId()).size(); i++) {
            appointData[0] = doctor.getAppointList(doctor.getId()).get(i).getId();
            appointData[1] = doctor.getAppointList(doctor.getId()).get(i).getHastaName();
            appointData[2] = doctor.getAppointList(doctor.getId()).get(i).getApDate();
            appointModel.addRow(appointData);
        }
        whourData = new Object[2];
        for (int i = 0; i < doctor.getWhourList(doctor.getId()).size(); i++) {
            whourData[0] = doctor.getWhourList(doctor.getId()).get(i).getId();
            whourData[1] = doctor.getWhourList(doctor.getId()).get(i).getWdate();
            whourModel.addRow(whourData);
        }
    }

    private void createUIComponents() {
        dc_date = new JDateChooser();
        whourModel = new DefaultTableModel();
        Object[] colWhour = new Object[2];
        colWhour[0] = "ID";
        colWhour[1] = "Tarih";
        whourModel.setColumnIdentifiers(colWhour);
        tbl_whour = new JTable(whourModel);

        appointModel = new DefaultTableModel();
        Object[] colAppoint = new Object[3];
        colAppoint[0] = "ID";
        colAppoint[1] = "Hasta";
        colAppoint[2] = "Tarih";
        appointModel.setColumnIdentifiers(colAppoint);
        tbl_appoint = new JTable(appointModel);



    }

    public void updateWhourModel(Doctor doctor) {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_whour.getModel();
        clearModel.setRowCount(0);
        for (int i = 0; i < doctor.getWhourList(doctor.getId()).size(); i++) {
            whourData[0] = doctor.getWhourList(doctor.getId()).get(i).getId();
            whourData[1] = doctor.getWhourList(doctor.getId()).get(i).getWdate();
            whourModel.addRow(whourData);
        }
    }
    public void updateAppointModel(Doctor doctor) {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_appoint.getModel();
        clearModel.setRowCount(0);
        for (int i = 0; i < doctor.getAppointList(doctor.getId()).size(); i++) {
            appointData[0] = doctor.getAppointList(doctor.getId()).get(i).getId();
            appointData[1] = doctor.getAppointList(doctor.getId()).get(i).getHastaName();
            appointData[2] = doctor.getAppointList(doctor.getId()).get(i).getApDate();
            appointModel.addRow(appointData);
        }
    }

    public static void main(String[] args) throws SQLException {
        Doctor doctor = new Doctor();
        DoctorGUI dGUI = new DoctorGUI(doctor);
        dGUI.setVisible(true);

    }

}
