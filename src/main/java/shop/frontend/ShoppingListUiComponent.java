package shop.frontend;

import com.sun.xml.internal.fastinfoset.stax.events.CommentEvent;

import javax.swing.*;
import java.awt.*;

public class ShoppingListUiComponent {

    private static final int HEIGHT = 50;
    private JTextField name;
    private JTextField quantity;

    public JTextField textInput() {
        return name = getjTextField(50, 50, 300);
    }

    public JTextField quantityInput() {
        quantity = getjTextField(410, 50, 65);
        itemQuantityNumberInputConfig(quantity);
        return quantity;
    }

    public JButton deleteBtn(int topMargin) {
        JButton button = getButton("X", 500, topMargin, 50, 15);
        deleteBtnUiConfig(button);
        return button;
    }

    private static void deleteBtnUiConfig(JButton button) {
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
    }

    public JButton addBtn() {
        return getButton("Add", 300, 120, 150, 15);
    }

    public JButton saveBtn() {
        return getButton("Save", 100, 120, 150, 15);
    }

    public static JTextField getjTextField(int x, int y, int width) {
        JTextField jTextField = new JTextField();
        jTextField.setBounds(x, y, width, HEIGHT);
        jTextField.setEditable(true);
        return jTextField;
    }

    private static JTextField itemQuantityNumberInput() {
        JTextField itemQuantity = getjTextField(410, 50, 65);
        itemQuantityNumberInputConfig(itemQuantity);
        return itemQuantity;
    }

    private static void itemQuantityNumberInputConfig(JTextField itemQuantity) {
        itemQuantity.setText("0");
        itemQuantity.setHorizontalAlignment(JTextField.CENTER);
    }

    public JButton quantityPlusBtn() {
        JButton button = getButton("+", 480, 50, 40, 10);
        button.addActionListener(e -> quantity.setText(incrementQuantity()));
        return button;
    }

    public JButton quantityMinusBtn() {
        JButton button = getButton("-", 365, 50, 40, 10);
        button.addActionListener(e -> quantity.setText(decrementQuantity()));
        return button;
    }

    private String decrementQuantity() {
        return String.valueOf(Integer.parseInt(quantity.getText()) - 1);
    }

    private String incrementQuantity() {
        return String.valueOf(Integer.parseInt(quantity.getText()) + 1);
    }

    private static JButton getButton(String text, int x, int y, int width, int textSize) {
        JButton saveBtn = new JButton(text);
        saveBtn.setBounds(x, y, width, HEIGHT);
        saveBtn.setFont(new Font("Arial", Font.PLAIN, textSize));
        return saveBtn;
    }

    public String getItemName() {
        return name.getText();
    }

    public String getItemQuantity() {
        return quantity.getText();
    }

    public JTextField getInputQuantity() {
        return quantity;
    }

    public JTextField getInputName() {
        return name;
    }

    public void setInputName(String text) {
        quantity.setText(text);
    }

    public void setInputQuantity(String text) {
        quantity.setText(text);
    }

}
