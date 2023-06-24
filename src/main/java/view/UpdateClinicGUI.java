package view;

import helper.Helper;
import model.Clinic;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;




public class UpdateClinicGUI extends JFrame{
    private JTextField tf_clinicName;
    private JButton bt_updateClinic;
    private JPanel w_pane;
    private Clinic clinic;

    public UpdateClinicGUI(Clinic clinic) {

        this.clinic = clinic;
        setContentPane(w_pane);
        setTitle("");
        setSize(225, 150);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);


        bt_updateClinic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Helper.confirm("sure")){
                    clinic.updateClinic(clinic.getId(), tf_clinicName.getText());
                    Helper.showMsg("success");
                    dispose();
                }
            }
        });
    }
}


