package view;

import helper.*;
import model.Bashekim;
import model.Clinic;
import model.Doctor;
import model.User;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;


public class BashekimGUI extends JFrame {

    private JPanel w_pane;
    private JButton bt_cikisYap;
    private JLabel lbl_bashekimHg;
    private JTabbedPane w_tab;
    private JTextField tf_adsoyad;
    private JTable tbl_doktor;
    private JTextField tf_tcNo;
    private JTextField tf_id;
    private JButton bt_ekle;
    private JButton bt_sil;
    private JScrollPane scb_doktor;
    private JPasswordField pf_sifre;
    private JPanel w_clinic;
    private JScrollPane scp_klinik;
    private JTextField tf_clinic;
    private JButton bt_addClinic;
    private JTable tbl_clinic;
    private JComboBox cbo_doctor;
    private JButton bt_addEmployee;
    private JScrollPane scp_employee;
    private JTable tbl_employee;
    private JButton bt_employeeSelect;
    private JTable tbl_appoint;
    private DefaultTableModel doktorModel;
    private Object[] doktorData;
    private DefaultTableModel clinicModel;
    private Object[] clinicData;
    private JPopupMenu clinicMenu, appointMenu;
    private DefaultTableModel appointModel;
    private Object[] appointData = null;



    public BashekimGUI(Bashekim bashekim) throws SQLException {
        Clinic clinic = new Clinic();

        lbl_bashekimHg.setText("Hoşgeldiniz, Sayın " + bashekim.getName());
        setContentPane(w_pane);
        setTitle("Hastane Yönetim Sistemi");
        setSize(750, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        bt_ekle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tf_adsoyad.getText().length() == 0 || pf_sifre.getText().length() == 0 || tf_tcNo.getText().length() == 0) {
                    Helper.showMsg("fill");
                } else {
                    boolean control = bashekim.add(tf_tcNo.getText(), pf_sifre.getText(), tf_adsoyad.getText());
                    if (control) {
                        Helper.showMsg("success");
                        tf_tcNo.setText(null);
                        tf_adsoyad.setText(null);
                        pf_sifre.setText(null);
                        updateDoctorModel();
                    }
                }
            }
        });
        tbl_doktor.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {
                    tf_id.setText(tbl_doktor.getValueAt(tbl_doktor.getSelectedRow(), 0).toString());
                } catch (Exception ex) {

                }
            }
        });
        tbl_doktor.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int selectID = Integer.parseInt(tbl_doktor.getValueAt(tbl_doktor.getSelectedRow(), 0).toString());
                    String selectName = tbl_doktor.getValueAt(tbl_doktor.getSelectedRow(), 1).toString();
                    String selectTcNo = tbl_doktor.getValueAt(tbl_doktor.getSelectedRow(), 2).toString();
                    String selectPass = tbl_doktor.getValueAt(tbl_doktor.getSelectedRow(), 3).toString();

                    boolean control = bashekim.updateDoctor(selectID, selectTcNo, selectPass, selectName);
                }
            }
        });
        bt_sil.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tf_id.getText().length() == 0) {
                    Helper.showMsg("Lütfen bir doktor seçiniz!");
                } else {
                    if (Helper.confirm("sure")) {
                        int selectedID = Integer.parseInt(tf_id.getText());
                        boolean control = bashekim.deleteDoctor(selectedID);
                        if (control) {
                            Helper.showMsg("success");
                            tf_id.setText(null);
                            updateDoctorModel();
                        }
                    }
                }
            }
        });
        bt_addClinic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tf_clinic.getText().length() == 0) {
                    Helper.showMsg("fill");
                } else {
                    if (clinic.addClinic(tf_clinic.getText())) {
                        Helper.showMsg("success");
                        tf_clinic.setText(null);
                        try {
                            updateClinicModel();
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
        });
        tbl_clinic.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                int selectedRow = tbl_clinic.rowAtPoint(point);
                tbl_clinic.setRowSelectionInterval(selectedRow, selectedRow);
            }
        });
        cbo_doctor.addActionListener(e -> {
            bashekim.getDoctorList();
            JComboBox c = (JComboBox) e.getSource();
            Item item = (Item) c.getSelectedItem();
        });

        DefaultTableModel employeeModel = new DefaultTableModel();
        Object[] colEmployee = new Object[2];
        colEmployee[0] = "ID";
        colEmployee[1] = " Ad Soyad";
        employeeModel.setColumnIdentifiers(colEmployee);
        Object[] employeeData = new Object[2];

        bt_addEmployee.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selRow = tbl_clinic.getSelectedRow();
                if (selRow >= 0) {
                    String selClinic = tbl_clinic.getModel().getValueAt(selRow, 0).toString();
                    int selClinicID = Integer.parseInt(selClinic);
                    Item doctorItem = (Item) cbo_doctor.getSelectedItem();
                    try {
                        boolean control = bashekim.add(doctorItem.getKey(), selClinicID);
                        if (control) {
                            Helper.showMsg("success");
                            DefaultTableModel clearModel = (DefaultTableModel) tbl_employee.getModel();
                            clearModel.setRowCount(0);
                            for(int i=0; i< bashekim.getClinicDoctorList(selClinicID).size();i++){
                                employeeData[0] = bashekim.getClinicDoctorList(selClinicID).get(i).getId();
                                employeeData[1] = bashekim.getClinicDoctorList(selClinicID).get(i).getName();
                                employeeModel.addRow(employeeData);
                            }
                            tbl_employee.setModel(employeeModel);
                        } else {
                            Helper.showMsg("Hata!");
                        }
                    } catch (NullPointerException ex) {
                        Helper.showMsg(ex.getMessage());
                    }
                } else {
                    Helper.showMsg("Lütfen bir polikinlik seçiniz.");
                }
            }
        });

        bt_employeeSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selRow = tbl_clinic.getSelectedRow();
                if ( selRow >= 0){
                    String selClinic = tbl_clinic.getModel().getValueAt(selRow, 0).toString();
                    int selClinicID = Integer.parseInt(selClinic);
                    DefaultTableModel clearModel = (DefaultTableModel) tbl_employee.getModel();
                    clearModel.setRowCount(0);

                    for(int i=0; i< bashekim.getClinicDoctorList(selClinicID).size();i++){
                        employeeData[0] = bashekim.getClinicDoctorList(selClinicID).get(i).getId();
                        employeeData[1] = bashekim.getClinicDoctorList(selClinicID).get(i).getName();
                        employeeModel.addRow(employeeData);
                    }
                    tbl_employee.setModel(employeeModel);
                }else{
                    Helper.showMsg("Lütfen bir poliklinik seçiniz!");
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
        appointData = new Object[4];
        for (int i = 0; i < bashekim.getAppointList().size(); i++) {
            appointData[0] = bashekim.getAppointList().get(i).getId();
            appointData[1] = bashekim.getAppointList().get(i).getDoctorName();
            appointData[2] = bashekim.getAppointList().get(i).getHastaName();
            appointData[3] = bashekim.getAppointList().get(i).getApDate();
            appointModel.addRow(appointData);
        }
        cbo_doctor.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                cbo_doctor.removeAllItems();
                for (User doctor : bashekim.getDoctorList()) {
                    cbo_doctor.addItem(new Item(doctor.getId(), doctor.getName()));
                }
            }
        });
    }

    public void updateDoctorModel() {
        Bashekim bashekim = new Bashekim();
        DefaultTableModel clearModel = (DefaultTableModel) tbl_doktor.getModel();
        clearModel.setRowCount(0);
        for (int i = 0; i < bashekim.getDoctorList().size(); i++) {
            doktorData[0] = bashekim.getDoctorList().get(i).getId();
            doktorData[1] = bashekim.getDoctorList().get(i).getName();
            doktorData[2] = bashekim.getDoctorList().get(i).getTcno();
            doktorData[3] = bashekim.getDoctorList().get(i).getPassword();
            doktorModel.addRow(doktorData);
        }
    }

    public void updateClinicModel() throws SQLException {
        Clinic clinic = new Clinic();
        DefaultTableModel clearModel = (DefaultTableModel) tbl_clinic.getModel();
        clearModel.setRowCount(0);
        for (int i = 0; i < clinic.getList().size(); i++) {
            clinicData[0] = clinic.getList().get(i).getId();
            clinicData[1] = clinic.getList().get(i).getName();
            clinicModel.addRow(clinicData);
        }

    }
    public void updateAppointModel() {
        Bashekim bashekim = new Bashekim();
        DefaultTableModel clearModel = (DefaultTableModel) tbl_appoint.getModel();
        clearModel.setRowCount(0);
        for (int i = 0; i < bashekim.getAppointList().size(); i++) {
            appointData[0] = bashekim.getAppointList().get(i).getId();
            appointData[1] = bashekim.getAppointList().get(i).getDoctorName();
            appointData[2] = bashekim.getAppointList().get(i).getHastaName();
            appointData[3] = bashekim.getAppointList().get(i).getApDate();
            appointModel.addRow(appointData);
        }
    }

    private void createUIComponents() throws SQLException {
        //Doktor Modeli
        Bashekim bashekim = new Bashekim();
        doktorModel = new DefaultTableModel();
        Object[] colDoktorName = new Object[4];
        colDoktorName[0] = "ID";
        colDoktorName[1] = "Ad Soyad";
        colDoktorName[2] = "TC NO";
        colDoktorName[3] = "Şifre";
        doktorModel.setColumnIdentifiers(colDoktorName);
        doktorData = new Object[4];
        for (int i = 0; i < bashekim.getDoctorList().size(); i++) {
            doktorData[0] = bashekim.getDoctorList().get(i).getId();
            doktorData[1] = bashekim.getDoctorList().get(i).getName();
            doktorData[2] = bashekim.getDoctorList().get(i).getTcno();
            doktorData[3] = bashekim.getDoctorList().get(i).getPassword();
            doktorModel.addRow(doktorData);
        }
        tbl_doktor = new JTable(doktorModel);

        //Poliklinik Modeli
        Clinic clinic = new Clinic();
        clinicModel = new DefaultTableModel();
        Object[] colClinicName = new Object[2];
        colClinicName[0] = "ID";
        colClinicName[1] = "Poliklinik Adı";
        clinicModel.setColumnIdentifiers(colClinicName);
        clinicData = new Object[2];
        for (int i = 0; i < clinic.getList().size(); i++) {
            clinicData[0] = clinic.getList().get(i).getId();
            clinicData[1] = clinic.getList().get(i).getName();
            clinicModel.addRow(clinicData);
        }
        tbl_clinic = new JTable(clinicModel);

        clinicMenu = new JPopupMenu();
        JMenuItem updateMenu = new JMenuItem("Güncelle");
        JMenuItem deleteMenu = new JMenuItem("Sil");
        clinicMenu.add(updateMenu);
        clinicMenu.add(deleteMenu);
        tbl_clinic.setComponentPopupMenu(clinicMenu);
        updateMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedID = Integer.parseInt(tbl_clinic.getValueAt(tbl_clinic.getSelectedRow(), 0).toString());
                try {
                    Clinic selectClinic = clinic.getFetch(selectedID);
                    UpdateClinicGUI updateClinicGUI = new UpdateClinicGUI(selectClinic);
                    updateClinicGUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    updateClinicGUI.setVisible(true);
                    updateClinicGUI.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            try {
                                updateClinicModel();
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        deleteMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Helper.confirm("sure")) {
                    int selectedID = Integer.parseInt(tbl_clinic.getValueAt(tbl_clinic.getSelectedRow(), 0).toString());
                    if (clinic.deleteClinic(selectedID)) {
                        Helper.showMsg("success");
                        try {
                            updateClinicModel();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        Helper.showMsg("error");
                    }
                }
            }
        });

        appointModel = new DefaultTableModel();
        Object[] colAppoint = new Object[4];
        colAppoint[0] = "ID";
        colAppoint[1] = "Doktor";
        colAppoint[2] = "Hasta";
        colAppoint[3] = "Tarih";
        appointModel.setColumnIdentifiers(colAppoint);
        tbl_appoint = new JTable(appointModel);

        appointMenu = new JPopupMenu();
        JMenuItem deleteAppointMenu = new JMenuItem("Sil");
        appointMenu.add(deleteAppointMenu);
        tbl_appoint.setComponentPopupMenu(appointMenu);
        deleteAppointMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Helper.confirm("sure")) {
                    int selRow = tbl_appoint.getSelectedRow();
                    String doctorName = tbl_appoint.getModel().getValueAt(selRow,1).toString();
                    String date = tbl_appoint.getModel().getValueAt(selRow,3).toString();
                    int selectedID = Integer.parseInt(tbl_appoint.getValueAt(tbl_appoint.getSelectedRow(), 0).toString());
                    try {
                        if (bashekim.deleteAppoint(selectedID) && bashekim.activateWhourStatus(doctorName,date)) {
                            Helper.showMsg("success");
                            updateAppointModel();
                        } else {
                            Helper.showMsg("error");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        cbo_doctor = new JComboBox();
        for (User doctor : bashekim.getDoctorList()) {
            cbo_doctor.addItem(new Item(doctor.getId(), doctor.getName()));
        }
    }

    public static void main(String[] args) throws SQLException {
        Bashekim bHekim = new Bashekim();
        BashekimGUI bGUI = new BashekimGUI(bHekim);
        bGUI.setVisible(true);
    }
}