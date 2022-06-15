package shop.frontend;

import shop.*;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class ShoppingListUi {

    private static final String SAVED_SUCCESSFULLY = "Items saved successfully!";
    private static final String SHOPPING_LIST_TITLE = "Shopping List";
    private static final int HEIGHT = 50;
    private static String errorMessage;

    private static Component alertComp;
    private static final List<Item> shoppingListItems = new LinkedList<>();
    private static final JFrame shoppingListPage = new JFrame();
    private static JTextField itemNameComp;
    private static JTextField itemQuantityComp;
    private static int itemCount = 0;
    private static final ShoppingListService shopService =
            new ShoppingListServiceImpl(new MySQLShoppingListDAO());

    public static void main(String[] args) {
        initItemComponent();
        loadComponentsToPage();
        initShoppingListPage();
        findAllItems();
    }

    private static void initItemComponent() {
        itemNameComp = itemNameTextInput();
        itemQuantityComp = itemQuantityNumberInput();
    }

    private static void loadComponentsToPage() {
        Component[] components = {itemNameComp, quantityInpMinusBtn(), itemQuantityComp,
                quantityPlusBtn(), addItemBtn(), saveBtn()};

        addComponentsToPage(components);
    }

    public static void addComponentsToPage(Component[] components) {
        for (Component component : components) {
            shoppingListPage.add(component);
        }
    }


    private static JTextField itemNameTextInput() {
        return getjTextField(50, 50, 300);
    }

    private static JTextField itemQuantityNumberInput() {
        JTextField itemQuantity = getjTextField(410, 50, 65);
        itemQuantityNumberInputConfig(itemQuantity);
        return itemQuantity;
    }

    public static JTextField getjTextField(int x, int y, int width) {
        JTextField itemName = new JTextField();
        itemName.setBounds(x, y, width, HEIGHT);
        itemName.setEditable(true);
        return itemName;
    }

    private static void itemQuantityNumberInputConfig(JTextField itemQuantity) {
        itemQuantity.setText("0");
        itemQuantity.setHorizontalAlignment(JTextField.CENTER);
    }

    private static JButton quantityInpMinusBtn() {
        JButton button = getButton("-", 365, 50, 40, 10);
        button.addActionListener(e -> itemQuantityComp.setText(decrementQuantity()));
        return button;
    }

    private static String decrementQuantity() {
        return String.valueOf(Integer.parseInt(itemQuantityComp.getText()) - 1);
    }

    private static JButton quantityPlusBtn() {
        JButton button = getButton("+", 480, 50, 40, 10);
        button.addActionListener(e -> itemQuantityComp.setText(incrementQuantity()));
        return button;
    }

    private static String incrementQuantity() {
        return String.valueOf(Integer.parseInt(itemQuantityComp.getText()) + 1);
    }

    private static JButton addItemBtn() {
        JButton addBtn = getButton("Add", 300, 120, 150, 15);
        addItemBtnAction(addBtn);
        return addBtn;
    }

    private static void addItemBtnAction(JButton addBtn) {
        addBtn.addActionListener(e -> {
            if (!isValidItems()) {
                throwAlert(Alert.error(errorMessage));
            } else {
                clearAlert();
                incrementItemCount();
                Item item = createUiItemOfClientItem(getTopMargin());
                addItemToPage(item);
                clearItemsInput();
            }
        });
    }


    private static void incrementItemCount() {
        itemCount++;
    }

    private static int getTopMargin() {
        return isFirstItem() ? 185 : computeTopMargin();
    }

    private static int computeTopMargin() {
        return 130 + (55 * itemCount);
    }

    private static boolean isFirstItem() {
        return itemCount == 1;
    }

    private static Item createUiItemOfClientItem(int topMargin) {
        return createItemUiComponent(itemNameComp.getText(), itemQuantityComp.getText(), topMargin);
    }

    private static boolean isValidItems() {
        boolean isValidItems = true;
        int quantity = Integer.parseInt(itemQuantityComp.getText());
        String itemName = itemNameComp.getText();

        if (itemName == null || itemName.isEmpty()) {
            isValidItems = false;
            errorMessage = "Please Enter the Item name.";
        } else if (quantity < 1 || quantity > 100) {
            isValidItems = false;
            errorMessage = "item quantity should have between 1 and 100";
        }

        return isValidItems;
    }

    private static Item createItemUiComponent(String name, String quantity, int topMargin) {
        Item itemUi = new Item(name, quantity, topMargin);
        itemUi.createItemComponent();
        return itemUi;
    }

    private static void addItemToPage(Item itemUi) {
        shoppingListItems.add(itemUi);
        for (Item item : shoppingListItems) {
            addItemToShoppingList(item);
            attachDeleteBtnToItem(item);
        }
    }

    private static void attachDeleteBtnToItem(Item item) {
        shoppingListPage.add(deleteBtn(item));
    }

    private static void addItemToShoppingList(Item item) {
        shoppingListPage.add(item.getNameComponent());
        shoppingListPage.add(item.getQuantityComponent());
        shoppingListPage.repaint();
    }

    private static JButton deleteBtn(Item item) {
        JButton button = getButton("X", 500, item.getTopMargin(), 50, 15);
        deleteBtnUiConfig(button);
        deleteBtnAction(item, button);
        return button;
    }

    private static void deleteBtnUiConfig(JButton button) {
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
    }

    private static void deleteBtnAction(Item item, JButton button) {
        button.addActionListener(e1 -> {
            shoppingListItems.remove(item);
            removeItemOfShoppingListPage(item);
            removeComponent(button);
            decrementItemCount();
        });
    }

    private static void removeItemOfShoppingListPage(Item item) {
        removeComponent(item.getQuantityComponent());
        removeComponent(item.getNameComponent());
    }

    public static void removeComponent(Component component) {
        shoppingListPage.getContentPane().remove(component);
        shoppingListPage.repaint();
        shoppingListPage.revalidate();
    }

    private static void decrementItemCount() {
        itemCount = itemCount != 0 ? itemCount - 1 : 0;
    }

    private static void clearItemsInput() {
        itemNameComp.setText("");
        itemQuantityComp.setText("0");
    }

    private static JButton saveBtn() {
        JButton saveBtn = getButton("Save", 100, 120, 150, 15);
        saveBtn.addActionListener(e -> saveItems());
        return saveBtn;
    }

    private static JButton getButton(String text, int x, int y, int width, int textSize) {
        JButton saveBtn = new JButton(text);
        saveBtn.setBounds(x, y, width, HEIGHT);
        saveBtn.setFont(new Font("Arial", Font.PLAIN, textSize));
        return saveBtn;
    }

    public static void saveItems() {
        try {
            shopService.saveItems(getItems());
            throwAlert(Alert.success(SAVED_SUCCESSFULLY));
        } catch (ItemNameException | ItemQuantityException e) {
            throwAlert(Alert.error(e.getMessage()));
        }
    }

    private static List<shop.Item> getItems() {
        List<shop.Item> items = new LinkedList<>();
        for (Item component : shoppingListItems) {
            items.add(new shop.Item(component.getName(), component.getQuantity()));
        }
        return items;
    }

    private static void throwAlert(Component alert) {
        clearAlert();
        alertComp = alert;
        shoppingListPage.add(alert);
    }

    private static void clearAlert() {
        if (alertComp != null)
            removeComponent(alertComp);
    }

    private static void initShoppingListPage() {
        shoppingListPage.setTitle(SHOPPING_LIST_TITLE);
        shoppingListPage.setSize(600, 600);
        shoppingListPage.setLayout(null);
        shoppingListPage.setVisible(true);
    }

    private static void findAllItems() {
        List<shop.Item> items = shopService.findAllItems();
        for (shop.Item item : items) {
            incrementItemCount();
            int topMargin = getTopMargin();
            Item itemUi = createItemUiComponentOfDatabaseItems(item.getName(), String.valueOf(item.getQuantity()), topMargin);
            shoppingListItems.add(itemUi);
            addItemToPage(itemUi);
        }
    }

    private static Item createItemUiComponentOfDatabaseItems(String name, String quantity, int topMargin) {
        return createItemUiComponent(name, quantity, topMargin);
    }
}