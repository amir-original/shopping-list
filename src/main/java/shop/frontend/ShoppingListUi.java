package shop.frontend;

import shop.*;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class ShoppingListUi {

    private static final String SHOPPING_LIST_TITLE = "Shopping List";
    private static final String FAIL_ITEM_NAME_MESSAGE = "Please Enter the Item name.";
    private static final String SAVED_SUCCESSFULLY = "Items saved successfully!";
    private static final String FAIL_ITEM_QUANTITY_MESSAGE = "item quantity should have between 1 and 100";
    private static final JFrame shoppingListPage = new JFrame();
    private static final ShoppingListUiComponent uiComponent = new ShoppingListUiComponent();
    private static final ShoppingListService shopService = new ShoppingListServiceImpl(new MySQLShoppingListDAO());
    private static final List<UiItem> shoppingListItems = new LinkedList<>();
    private static Component alertBox;
    private static String errorMessage = "";
    private static int itemCount = 0;


    public static void main(String[] args) {
        loadComponentsToPage();
        initShoppingListPage();
        findAllItems();
    }

    private static void loadComponentsToPage() {
        Component[] components = {uiComponent.nameInput(), uiComponent.quantityPlusBtn(),
                uiComponent.quantityInput(), uiComponent.quantityMinusBtn(), addItemBtn(), saveBtn()};

        addComponentsToPage(components);
    }

    public static void addComponentsToPage(Component[] components) {
        for (Component component : components) {
            addComponentToPage(component);
        }
    }

    private static void initShoppingListPage() {
        shoppingListPage.setTitle(SHOPPING_LIST_TITLE);
        shoppingListPage.setSize(600, 600);
        shoppingListPage.setLayout(null);
        shoppingListPage.setVisible(true);
    }

    private static void findAllItems() {
        List<Item> items = shopService.findAllItems();
        for (Item item : items) {
            incrementItemCount();
            UiItem itemUi = createUiItemOfDbItems(item.getName(), item.getStrQuantity());
            addItemToShoppingListItems(itemUi);
            addItemsToPage();
        }
    }

    private static void addItemToShoppingListItems(UiItem itemUi) {
        shoppingListItems.add(itemUi);
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
            if (!isValidItem())
                throwAlert(Alert.error(errorMessage));
            else
                refreshPageWhenClickAddBtn();
        });
    }

    private static void refreshPageWhenClickAddBtn() {
        incrementItemCount();
        UiItem item = createUiItemOfClientItem();
        addItemToShoppingListItems(item);
        addItemsToPage();
        clearAlertAndItemsInput();
    }

    private static void clearAlertAndItemsInput() {
        clearAlert();
        clearItemsInput();
    }

    private static boolean isValidItem() {
        boolean isValidItems = true;
        if (isItemNameNullOrEmpty(getItemName())) {
            isValidItems = false;
            setFailMessage(FAIL_ITEM_NAME_MESSAGE);
        } else if (isItemQuantityBetween(1, 100)) {
            isValidItems = false;
            setFailMessage(FAIL_ITEM_QUANTITY_MESSAGE);
        }
        return isValidItems;
    }

    private static boolean isItemNameNullOrEmpty(String itemName) {
        return itemName == null || itemName.isEmpty();
    }

    private static String getItemName() {
        return uiComponent.getItemName();
    }

    private static boolean isItemQuantityBetween(int min, int max) {
        int quantity = getIntItemQuantity();
        return quantity < min || quantity > max;
    }

    private static void setFailMessage(String failMessage) {
        errorMessage = failMessage;
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
        return createItemUiComponent(getItemName(), getStrItemQuantity());
    }

    private static String getStrItemQuantity() {
        return uiComponent.getItemQuantity();
    }

    private static Integer getIntItemQuantity() {
        return uiComponent.getIntItemQuantity();
    }

    private static UiItem createItemUiComponent(String name, String quantity) {
        UiItem itemUi = new UiItem(name, quantity, getTopMargin());
        itemUi.createItemComponent();
        return itemUi;
    }

    private static void addItemsToPage() {
        for (UiItem item : shoppingListItems) {
            addItemToShoppingList(item);
            attachDeleteBtnToItem(item);
        }
    }

    private static void addItemToShoppingList(UiItem item) {
        addComponentToPage(item.getNameComponent());
        addComponentToPage(item.getQuantityComponent());
        shoppingListPage.repaint();
    }

    private static void attachDeleteBtnToItem(UiItem item) {
        addComponentToPage(deleteBtn(item));
    }

    private static void addComponentToPage(Component component) {
        shoppingListPage.add(component);
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
            refreshPage();
        });
    }

    private static void refreshPage() {
        clearItemCount();
        removeAllElements();
    }

    private static void clearItemCount() {
        itemCount = 0;
    }

    private static void removeAllElements() {
        removeAllDeleteBtn();
        refreshAllItems();
    }

    private static void removeAllDeleteBtn() {
        for (Component component : getAllComponents())
            removeDeleteBtn(component);
    }

    private static void removeDeleteBtn(Component component) {
        if (isThisComponentBtn(component)) {
            JButton btn = (JButton) component;
            if (isDeleteBtn(btn))
                removeComponent(component);
        }
    }

    private static boolean isThisComponentBtn(Component component) {
        return component instanceof JButton;
    }

    private static boolean isDeleteBtn(JButton btn) {
        return btn.getText().equals("X");
    }

    private static void refreshAllItems() {
        for (UiItem shoppingListItem : shoppingListItems) {
            removeItemOfShoppingListPage(shoppingListItem);
            refreshShoppingListPage(shoppingListItem);
        }
    }

    private static void refreshShoppingListPage(UiItem shoppingListItem) {
        incrementItemCount();
        UiItem newItem = createItemUiComponent(shoppingListItem.getName(), shoppingListItem.getStrQuantity());
        addOneItemToPage(newItem);
        updateShoppingListItem(shoppingListItem, newItem);
    }

    private static void updateShoppingListItem(UiItem src, UiItem dst) {
        shoppingListItems.set(shoppingListItems.indexOf(src), dst);
    }

    private static Component[] getAllComponents() {
        return shoppingListPage.getContentPane().getComponents();
    }

    private static void addOneItemToPage(UiItem itemUi) {
        addItemToShoppingList(itemUi);
        attachDeleteBtnToItem(itemUi);
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
        alertBox = alert;
        addComponentToPage(alert);
    }

    private static void clearAlert() {
        if (isAlertBoxNotEmpty())
            removeComponent(alertBox);
    }

    private static boolean isAlertBoxNotEmpty() {
        return !isAlertBoxEmpty();
    }

    private static boolean isAlertBoxEmpty() {
        return alertBox == null;
    }
}