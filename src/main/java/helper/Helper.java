package helper;

import javax.swing.*;

public class Helper {

    public static void optionPaneChangeButtonText(){
        UIManager.put("OptionPane.cancelButtonText","İptal");
        UIManager.put("OptionPane.noButtonText","Hayır");
        UIManager.put("OptionPane.okButtonText","Tamam");
        UIManager.put("OptionPane.yesButtonText","Evet");
    }
    public static void showMsg(String str) {
        String msg;
        optionPaneChangeButtonText();
        switch (str) {
            case "fill":
                msg = "Lütfen tüm alanları doldurunuz.";
                break;
            case "success":
                msg = "İşlem Başarılı!";
                break;
            case "error":
                msg="Bir hata oluştu!";
            default:
                msg = str;
        }
        JOptionPane.showMessageDialog(null, msg, "Mesaj", JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirm(String str) {
        String msg;
        optionPaneChangeButtonText();
        switch (str) {
            case "sure":
                msg = "Bu işlemi gerçekleştirmek istiyor musun?";
                break;
            default:
                msg = str;
                break;
        }
        int res = JOptionPane.showConfirmDialog(null, msg,"Dikkat!", JOptionPane.YES_NO_OPTION);
        if (res == 0){
            return true;
        }
        else {
            return false;
        }
    }
}
