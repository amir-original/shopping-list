package shop.frontend;

import javax.swing.*;

public class UiItem {

    private static final int TEXT_WIDTH = 300;
    private static final int HEIGHT = 50;
    private static final int QUANTITY_WIDTH = 65;
    private final String name;
    private final String quantity;
    private final int itemCount;
    private JTextField nameComponent;
    private JTextField quantityComponent;


    public UiItem(String name, String quantity, int itemCount) {
        this.name = name;
        this.quantity = quantity;
        this.itemCount = itemCount;
        createItemComponent();
    }

    public void createItemComponent() {
        createNameComponent();
        createQuantityComponent();
    }

    public void createNameComponent() {
        nameComponent = createJText(50, name, TEXT_WIDTH, JTextField.LEFT);
    }

    public void createQuantityComponent() {
        quantityComponent = createJText(380, quantity, QUANTITY_WIDTH, JTextField.CENTER);
    }

    public JTextField createJText(int marginLeft, String text, int width, int textAlign) {
        JTextField jTextField = new JTextField();
        jTextField.setBounds(marginLeft, getTopMargin(), width, HEIGHT);
        jTextFieldConfig(textAlign, jTextField);
        jTextField.setText(text);
        return jTextField;
    }

    private void jTextFieldConfig(int textAlign, JTextField jTextField) {
        jTextField.setEditable(false);
        jTextField.setHorizontalAlignment(textAlign);
    }

    public JTextField getNameComponent() {
        return nameComponent;
    }

    public JTextField getQuantityComponent() {
        return quantityComponent;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return Integer.parseInt(quantity);
    }

    public String getStrQuantity() {
        return quantity;
    }

    public int getTopMargin() {
        return isFirstItem() ? 185 : computeTopMargin();
    }

    private int computeTopMargin() {
        return 130 + (55 * itemCount);
    }

    private boolean isFirstItem() {
        return itemCount == 1;
    }

}
