package shop.frontend;

import shop.*;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class ShoppingListUi {

    private static final String SAVED_SUCCESSFULLY = "Items saved successfully!";
    private static final String SHOPPING_LIST_TITLE = "Shopping List";
    private static final List<UiItem> shoppingListItems = new LinkedList<>();
    private static final JFrame shoppingListPage = new JFrame();
    private static String errorMessage;
    private static Component alertComp;
    private static int itemCount = 0;
    private static final ShoppingListUiComponent uiComponent = new ShoppingListUiComponent();
    private static final ShoppingListService shopService =
            new ShoppingListServiceImpl(new MySQLShoppingListDAO());

    public static void main(String[] args) {
        loadComponentsToPage();
        initShoppingListPage();
        findAllItems();
    }

    private static void loadComponentsToPage() {
        Component[] components = {uiComponent.textInput(), uiComponent.quantityPlusBtn(), uiComponent.quantityInput(),
                uiComponent.quantityMinusBtn(), addItemBtn(), saveBtn()};

        addComponentsToPage(components);
    }

    public static void addComponentsToPage(Component[] components) {
        for (Component component : components) {
            addComponentToPage(component);
        }
    }

    private static void findAllItems() {
        List<Item> items = shopService.findAllItems();
        for (Item item : items) {
            incrementItemCount();
            UiItem itemUi = createUiItemOfDbItems(item.getName(), String.valueOf(item.getQuantity()));
            addItemToPage(itemUi);
        }
    }

    private static UiItem createUiItemOfDbItems(String name, String quantity) {
        return createItemUiComponent(name, quantity);
    }

    private static JButton addItemBtn() {
        JButton addBtn = uiComponent.addBtn();
        addItemBtnAction(addBtn);
        return addBtn;
    }

    private static void addItemBtnAction(JButton addBtn) {
        addBtn.addActionListener(e -> {
            if (!isValidItem()) {
                throwAlert(Alert.error(errorMessage));
            } else {
                clearAlert();
                incrementItemCount();
                UiItem item = createUiItemOfClientItem();
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

    private static UiItem createUiItemOfClientItem() {
        return createItemUiComponent(getItemName(), getItemQuantity());
    }

    private static boolean isValidItem() {
        boolean isValidItems = true;
        if (isItemNameNullOrEmpty(getItemName())) {
            isValidItems = false;
            setFailMessage("Please Enter the Item name.");
        } else if (isItemQuantityBetween(1, 100)) {
            isValidItems = false;
            setFailMessage("item quantity should have between 1 and 100");
        }
        return isValidItems;
    }

    private static String getItemName() {
        return uiComponent.getItemName();
    }

    private static boolean isItemQuantityBetween(int min, int max) {
        int quantity = Integer.parseInt(getItemQuantity());
        return quantity < min || quantity > max;
    }

    private static String getItemQuantity() {
        return uiComponent.getItemQuantity();
    }

    private static boolean isItemNameNullOrEmpty(String itemName) {
        return itemName == null || itemName.isEmpty();
    }

    private static void setFailMessage(String failMessage) {
        errorMessage = failMessage;
    }

    private static UiItem createItemUiComponent(String name, String quantity) {
        UiItem itemUi = new UiItem(name, quantity, getTopMargin());
        itemUi.createItemComponent();
        return itemUi;
    }

    private static void addItemToPage(UiItem itemUi) {
        shoppingListItems.add(itemUi);
        for (UiItem item : shoppingListItems) {
            addItemToShoppingList(item);
            attachDeleteBtnToItem(itemUi);
        }
    }

    private static void attachDeleteBtnToItem(UiItem item) {
        addComponentToPage(deleteBtn(item));
    }

    private static void addItemToShoppingList(UiItem item) {
        addComponentToPage(item.getNameComponent());
        addComponentToPage(item.getQuantityComponent());
        shoppingListPage.repaint();
    }

    private static JButton deleteBtn(UiItem item) {
        JButton button = uiComponent.deleteBtn(item.getTopMargin());
        deleteBtnAction(item, button);
        return button;
    }

    private static void deleteBtnAction(UiItem item, JButton button) {
        button.addActionListener(e1 -> {
            shoppingListItems.remove(item);
            removeItemOfShoppingListPage(item);
            removeComponent(button);
            decrementItemCount();
        });
    }

    private static void removeItemOfShoppingListPage(UiItem item) {
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
        uiComponent.setInputName("");
        uiComponent.setInputQuantity("0");
    }

    private static JButton saveBtn() {
        JButton saveBtn = uiComponent.saveBtn();
        saveBtn.addActionListener(e -> saveItems());
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

    private static List<Item> getItems() {
        List<Item> items = new LinkedList<>();
        for (UiItem component : shoppingListItems) {
            items.add(new Item(component.getName(), component.getQuantity()));
        }
        return items;
    }

    private static void throwAlert(Component alert) {
        clearAlert();
        alertComp = alert;
        addComponentToPage(alert);
    }

    private static void addComponentToPage(Component alert) {
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

}