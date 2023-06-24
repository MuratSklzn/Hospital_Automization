package view;

import helper.Helper;
import model.Doctor;
import model.Hasta;
import model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class RegisterGUI extends JFrame {
    private JPanel w_pane;
    private JTextField tf_rName;
    private JTextField tf_rTcNo;
    private JPasswordField pf_rPass;
    private JButton bt_register;
    private JButton bt_backto;
    private Hasta hasta = new Hasta();

    public RegisterGUI() {
        setContentPane(w_pane);
        setTitle("Login");
        setSize(350, 350);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
        bt_register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(tf_rTcNo.getText().length()== 0 || pf_rPass.getText().length() == 0 || tf_rName.getText().length() == 0){
                    Helper.showMsg("fill");
                }else{
                    try {
                        boolean control = hasta.register(tf_rTcNo.getText(),pf_rPass.getText(), tf_rName.getText());
                        if(control){
                            Helper.showMsg("success");
                            LoginGUI loginGUI = new LoginGUI();
                            loginGUI.setVisible(true);
                            dispose();
                        }else{
                            Helper.showMsg("error");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        bt_backto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginGUI loginGUI = new LoginGUI();
                loginGUI.setVisible(true);
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        RegisterGUI rGUI = new RegisterGUI();
    }
}
