package shop.frontend;

import javax.swing.*;
import java.awt.*;

public class Alert {


    public static JTextField success(String text) {
        JTextField successField = getjTextField(60, 0, 600);
        successField.setText(text);
        successMessageConfig(successField);
        return successField;
    }

    private static void successMessageConfig(JTextField successField) {
        successField.setForeground(Color.BLACK);
        jTextNoticeMessageConfig(successField);
    }

    public static JTextField error(String text) {
        JTextField errorField = getjTextField(60, 0, 600);
        errorMessageConfig(errorField);
        errorField.setText(text);
        return errorField;
    }

    private static void errorMessageConfig(JTextField error) {
        error.setForeground(Color.RED);
        jTextNoticeMessageConfig(error);
    }

    private static void jTextNoticeMessageConfig(JTextField errorField) {
        errorField.setEditable(false);
        errorField.setOpaque(false);
        errorField.setBorder(BorderFactory.createEmptyBorder());
        errorField.setFont(new Font("Arial", Font.PLAIN, 20));
    }

    public static JTextField getjTextField(int x, int y, int width) {
        JTextField jTextField = new JTextField();
        jTextField.setBounds(x, y, width, 50);
        return jTextField;
    }
}
