package shop.frontend;

import shop.*;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class ShoppingListUi {

    private JFrame shoppingListPage;
    private ShoppingListComponent uiComponent;
    private ShoppingListService shopService;
    private List<ShoppingListUiItem> shoppingListItems;
    private Component alertBox;
    private int itemCount = 0;

    public static void main(String[] args) {
        ShoppingListUi ui = new ShoppingListUi();
        ui.setUp();
        ui.loadComponentsToPage();
        ui.initConfigShoppingListPage();
        ui.findAllItems();
    }

    private void setUp() {
        shoppingListPage = new JFrame();
        uiComponent = new ShoppingListComponent();
        shopService = new ShoppingListServiceImpl(new MySQLShoppingListDAO());
        shoppingListItems = new LinkedList<>();
    }

    private void loadComponentsToPage() {
        Component[] components = {uiComponent.nameInput(), uiComponent.quantityPlusBtn(),
                uiComponent.quantityInput(), uiComponent.quantityMinusBtn(), addItemBtn(), saveBtn()};

        addComponentsToPage(components);
    }

    private void addComponentsToPage(Component[] components) {
        for (Component component : components)
            addComponentToPage(component);
    }

    private void addComponentToPage(Component component) {
        shoppingListPage.add(component);
        shoppingListPage.repaint();
    }

    private void initConfigShoppingListPage() {
        shoppingListPage.setTitle("Shopping List");
        shoppingListPage.setSize(600, 600);
        shoppingListPage.setLayout(null);
        shoppingListPage.setVisible(true);
    }

    private void findAllItems() {
        putAllItemsOnPage(shopService.findAllItems());
    }

    private void putAllItemsOnPage(List<Item> items) {
        for (Item item : items)
            putItemOnPage(item.getName(),item.getStrQuantity());
    }

    private void putItemOnPage(String itemName, String itemQuantity) {
        incrementItemCount();
        ShoppingListUiItem uiItem = createUiItemComponent(itemName, itemQuantity);
        refreshPageItemsWith(uiItem);
    }

    private void incrementItemCount() {
        itemCount++;
    }

    private ShoppingListUiItem createUiItemComponent(String name, String quantity) {
        return new ShoppingListUiItem(name, quantity, getTopMargin());
    }

    private void refreshPageItemsWith(ShoppingListUiItem uiItem) {
        addItemToShoppingListItems(uiItem);
        addItemsToPage();
    }

    private void addItemToShoppingListItems(ShoppingListUiItem itemUi) {
        shoppingListItems.add(itemUi);
    }

    private void addItemsToPage() {
        for (ShoppingListUiItem item : shoppingListItems) {
            addItemToShoppingList(item);
            attachDeleteBtnToItem(item);
        }
    }

    private void addItemToShoppingList(ShoppingListUiItem item) {
        addComponentToPage(item.getNameComponent());
        addComponentToPage(item.getQuantityComponent());
    }

    private void attachDeleteBtnToItem(ShoppingListUiItem item) {
        addComponentToPage(deleteBtn(item));
    }

    private JButton deleteBtn(ShoppingListUiItem item) {
        JButton button = uiComponent.deleteBtn(item.getTopMargin());
        addActionToDeleteBtn(button,item);
        return button;
    }

    private void addActionToDeleteBtn(JButton button,ShoppingListUiItem item) {
        button.addActionListener(e1 -> {
            shoppingListItems.remove(item);
            removeItemOfShoppingListPage(item);
            refreshPage();
        });
    }

    private void removeItemOfShoppingListPage(ShoppingListUiItem item) {
        removeComponent(item.getQuantityComponent());
        removeComponent(item.getNameComponent());
    }

    private void refreshPage() {
        clearItemCount();
        removeAllElements();
    }

    private void clearItemCount() {
        itemCount = 0;
    }

    private void removeAllElements() {
        removeAllDeleteBtn();
        refreshAllItems();
    }

    private void removeAllDeleteBtn() {
        for (Component component : getAllComponents())
            removeDeleteBtn(component);
    }

    private Component[] getAllComponents() {
        return shoppingListPage.getContentPane().getComponents();
    }

    private void removeDeleteBtn(Component component) {
        if (uiComponent.isThisComponentBtn(component))
            if (uiComponent.isDeleteBtn((JButton) component))
                removeComponent(component);
    }

    private void removeComponent(Component component) {
        shoppingListPage.getContentPane().remove(component);
        shoppingListPage.repaint();
        shoppingListPage.revalidate();
    }

    private void refreshAllItems() {
        for (ShoppingListUiItem shoppingListItem : shoppingListItems) {
            removeItemOfShoppingListPage(shoppingListItem);
            refreshShoppingListPage(shoppingListItem);
        }
    }

    private void refreshShoppingListPage(ShoppingListUiItem shoppingListItem) {
        incrementItemCount();
        ShoppingListUiItem newItem = createUiItemComponent(shoppingListItem.getName(), shoppingListItem.getStrQuantity());
        addOneItemToPage(newItem);
        updateShoppingListItem(shoppingListItem, newItem);
    }

    private void addOneItemToPage(ShoppingListUiItem itemUi) {
        addItemToShoppingList(itemUi);
        attachDeleteBtnToItem(itemUi);
    }

    private void updateShoppingListItem(ShoppingListUiItem src, ShoppingListUiItem dst) {
        shoppingListItems.set(shoppingListItems.indexOf(src), dst);
    }

    private JButton addItemBtn() {
        JButton addBtn = uiComponent.addBtn();
        addItemBtnAction(addBtn);
        return addBtn;
    }

    private void addItemBtnAction(JButton addBtn) {
        addBtn.addActionListener(e -> {
            if (!uiComponent.isValidItem())
                throwAlert(Alert.error(uiComponent.getErrorMessage()));
            else
                refreshPageWhenClickAddBtn();
        });
    }

    private void refreshPageWhenClickAddBtn() {
        putItemOnPage(uiComponent.getItemName(),uiComponent.getItemQuantity());
        clearAlertAndItemsInput();
    }

    private void clearAlertAndItemsInput() {
        clearAlert();
        uiComponent.clearItemsInput();
    }

    private JButton saveBtn() {
        JButton saveBtn = uiComponent.saveBtn();
        saveBtn.addActionListener(e -> saveItems());
        return saveBtn;
    }

    private void saveItems() {
        try {
            shopService.saveItems(getItems());
            throwAlert(Alert.success(Message.SAVED_SUCCESSFULLY));
        } catch (ItemNameException | ItemQuantityException e) {
            throwAlert(Alert.error(e.getMessage()));
        }
    }

    private List<Item> getItems() {
        List<Item> items = new LinkedList<>();
        for (ShoppingListUiItem uiItem : shoppingListItems)
            items.add(new Item(uiItem.getName(), uiItem.getQuantity()));

        return items;
    }

    private int getTopMargin() {
        return isFirstItem() ? 185 : computeTopMargin();
    }

    private int computeTopMargin() {
        return 130 + (55 * itemCount);
    }

    private boolean isFirstItem() {
        return itemCount == 1;
    }

    private void throwAlert(Component alert) {
        clearAlert();
        alertBox = alert;
        addComponentToPage(alert);
    }

    private void clearAlert() {
        if (isAlertBoxNotEmpty())
            removeComponent(alertBox);
    }

    private boolean isAlertBoxNotEmpty() {
        return !isAlertBoxEmpty();
    }

    private boolean isAlertBoxEmpty() {
        return alertBox == null;
    }
} 