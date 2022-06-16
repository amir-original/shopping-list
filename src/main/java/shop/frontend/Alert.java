package shop.frontend;

import javax.swing.*;
import java.awt.*;

public class Alert {

    public static JTextField success(String text){
        JTextField successJText = getjTextField(60, 0, 600);
        successJText.setText(text);
        successMessageConfig(successJText);
        return successJText;
    }

    private static void successMessageConfig(JTextField jTextField) {
        jTextField.setForeground(Color.BLUE);
        jTextNoticeMessageConfig(jTextField);
    }

    public static JTextField error(String text){
        JTextField errorJText = getjTextField(60, 0, 600);
        errorConfig(errorJText);
        errorJText.setText(text);
        return errorJText;
    }

    private static void errorConfig(JTextField button) {
        button.setForeground(Color.RED);
        jTextNoticeMessageConfig(button);
    }

    private static void jTextNoticeMessageConfig(JTextField error) {
        error.setEditable(false);
        error.setOpaque(false);
        error.setBorder(BorderFactory.createEmptyBorder());
        error.setFont(new Font("Arial", Font.PLAIN, 20));
    }

    public static JTextField getjTextField(int x, int y, int width) {
        JTextField itemName = new JTextField();
        itemName.setBounds(x, y, width, 50);
        return itemName;
    }

}
