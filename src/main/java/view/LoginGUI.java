package view;

import helper.DBConnection;
import helper.Helper;
import model.Bashekim;
import model.Doctor;
import model.Hasta;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginGUI extends JFrame {
    private JTabbedPane w_tabpane;
    private JPanel w_pane;
    private JPasswordField pf_hastaPass;
    private JTextField tf_hastaTc;
    private JButton bt_hastaGiris;
    private JButton bt_kaydol;
    private JLabel lbl_baslik;
    private JLabel lbl_hastaTc;
    private JLabel lbl_HastaSifre;
    private JButton bt_doktorLogin;
    private JLabel lbl_doktorTc;
    private JLabel lbl_doktorSifre;
    private JPasswordField pf_doktorPass;
    private JTextField tf_doktorTc;
    private JPanel w_hastaLogin;
    private JPanel w_doktorLogin;

    private DBConnection conn = new DBConnection();

    public LoginGUI() {

        setContentPane(w_pane);
        setTitle("Login");
        setSize(350, 350);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);

        bt_doktorLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                {
                    boolean check = true;
                    try {
                        if (tf_doktorTc.getText().length() == 0 || pf_doktorPass.getText().length() == 0) {
                            throw new NullPointerException("fill");
                        }
                        Connection con = conn.connDb();
                        Statement st = con.createStatement();
                        ResultSet rs = st.executeQuery("SELECT * FROM user");
                        while (rs.next()) {
                            if (tf_doktorTc.getText().equals(rs.getString("tcno")) && pf_doktorPass.getText().equals(rs.getString("password"))) {
                                if (rs.getString("type").equals("bashekim")) {
                                    Bashekim bHekim = new Bashekim();
                                    bHekim.setId(rs.getInt("id"));
                                    bHekim.setPassword("password");
                                    bHekim.setTcno(rs.getString("tcno"));
                                    bHekim.setName(rs.getString("name"));
                                    bHekim.setType(rs.getString("type"));
                                    BashekimGUI bGUI = new BashekimGUI(bHekim);
                                    setTitle("Hastane Yönetim Sistemi");
                                    bGUI.setVisible(true);
                                    dispose();
                                    check = false;
                                } else if (rs.getString("type").equals("doktor")) {
                                    Doctor doctor = new Doctor();
                                    doctor.setId(rs.getInt("id"));
                                    doctor.setPassword("password");
                                    doctor.setTcno(rs.getString("tcno"));
                                    doctor.setName(rs.getString("name"));
                                    doctor.setType(rs.getString("type"));
                                    DoctorGUI dGUI = new DoctorGUI(doctor);
                                    setTitle("Hastane Yönetim Sistemi");
                                    dGUI.setVisible(true);
                                    dispose();
                                    check = false;
                                }
                            }

                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    } catch (NullPointerException ex) {
                        Helper.showMsg(ex.getMessage());
                    }
                    if (check){
                        Helper.showMsg("TC No veya Şifre Hatalı.");
                    }
                }
            }
        });
        bt_kaydol.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegisterGUI registerGUI = new RegisterGUI();
                registerGUI.setVisible(true);
                dispose();
            }
        });
        bt_hastaGiris.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean check = true;
                try {
                if (tf_hastaTc.getText().length() == 0 || pf_hastaPass.getText().length() == 0) {
                    throw new NullPointerException("fill");
                }
                    Connection con = conn.connDb();
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery("SELECT * FROM user");
                    while (rs.next()) {
                        if (tf_hastaTc.getText().equals(rs.getString("tcno")) && pf_hastaPass.getText().equals(rs.getString("password"))) {
                            if (rs.getString("type").equals("hasta")) {
                                Hasta hasta = new Hasta();
                                hasta.setId(rs.getInt("id"));
                                hasta.setPassword("password");
                                hasta.setTcno(rs.getString("tcno"));
                                hasta.setName(rs.getString("name"));
                                hasta.setType(rs.getString("type"));
                                HastaGUI hGUI = new HastaGUI(hasta);
                                setTitle("Hastane Randevu Sistemi");
                                hGUI.setVisible(true);
                                dispose();
                                check = false;
                            }
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                catch (NullPointerException ex) {
                    Helper.showMsg(ex.getMessage());
                }
                if (check){
                    Helper.showMsg("TC No veya Şifre Hatalı.");

                }
            }
        });
    }

}


