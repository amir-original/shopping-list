package shop.frontend;

import javax.swing.*;
import java.awt.*;

public class ShoppingListComponent {

    private static final int HEIGHT = 50;
    private JTextField name;
    private JTextField quantity;
    private String errorMessage;

    public JTextField nameInput() {
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

    private static void itemQuantityNumberInputConfig(JTextField itemQuantity) {
        itemQuantity.setText("0");
        itemQuantity.setHorizontalAlignment(JTextField.CENTER);
    }

    public JButton quantityPlusBtn() {
        JButton button = getButton("+", 480, 50, 40, 10);
        button.addActionListener(e -> quantity.setText(incrementQuantity()));
        return button;
    }

    private String incrementQuantity() {
        int q = getIntItemQuantity();
        int incrementQuantity = q < 100 ? q + 1 : 100;
        return convertToString(incrementQuantity);
    }

    public JButton quantityMinusBtn() {
        JButton button = getButton("-", 365, 50, 40, 10);
        button.addActionListener(e -> quantity.setText(decrementQuantity()));
        return button;
    }

    private static JButton getButton(String text, int x, int y, int width, int textSize) {
        JButton saveBtn = new JButton(text);
        saveBtn.setBounds(x, y, width, HEIGHT);
        saveBtn.setFont(new Font("Arial", Font.PLAIN, textSize));
        return saveBtn;
    }

    private String decrementQuantity() {
        int q = getIntItemQuantity();
        int decrementQuantity = q > 0 ? q - 1 : 0;
        return convertToString(decrementQuantity);
    }

    private String convertToString(int i) {
        return String.valueOf(i);
    }

    public String getItemQuantity() {
        return quantity.getText();
    }

    public int getIntItemQuantity() {
        return convertToInt(getItemQuantity());
    }

    private int convertToInt(String str) {
        return Integer.parseInt(str);
    }

    public void setInputName(String text) {
        name.setText(text);
    }

    public void setInputQuantity(String text) {
        quantity.setText(text);
    }

    boolean isDeleteBtn(JButton btn) {
        return btn.getText().equals("X");
    }

    boolean isThisComponentBtn(Component component) {
        return component instanceof JButton;
    }

    boolean isValidItem() {
        boolean isValidItem = true;
        if (isItemNameNullOrEmpty(getItemName()) || isWhiteSpace())
            isValidItem = notValidItem(Message.FAIL_ITEM_NAME);
        else if (isItemQuantityBetween1And100())
            isValidItem = notValidItem(Message.FAIL_ITEM_QUANTITY);

        return isValidItem;
    }

    boolean isItemNameNullOrEmpty(String itemName) {
        return itemName == null || itemName.isEmpty();
    }

    public String getItemName() {
        return name.getText();
    }

    boolean isWhiteSpace() {
        return getItemName().matches("^\\s*$");
    }

    private boolean notValidItem(String failMessage) {
        setFailMessage(failMessage);
        return false;
    }

    boolean isItemQuantityBetween1And100() {
        int quantity = getIntItemQuantity();
        return quantity < 1 || quantity > 100;
    }

    private void setFailMessage(String failMessage) {
        errorMessage = failMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void clearItemsInput() {
        setInputName("");
        setInputQuantity("0");
    }
}
