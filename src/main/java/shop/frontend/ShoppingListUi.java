package shop.frontend;

import shop.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ShoppingListUi {

    ShoppingListPage shoppingListPage;
    private ShoppingListComponent uiComponent;
    private ShoppingListService shopService;
    private ShoppingListItems shoppingListItems;

    public static void main(String[] args) {
        ShoppingListUi ui = new ShoppingListUi();
        ui.setUp();
        ui.loadComponentsToPage();
        ui.putTheSavedItemsOnPage();
    }

    private void setUp() {
        shoppingListPage = new ShoppingListPage("ShoppingList", 600, 600);
        shoppingListPage.generate();
        uiComponent = new ShoppingListComponent();
        shopService = new ShoppingListServiceImpl(new MySQLShoppingListDAO());
        shoppingListItems = new ShoppingListItems();
    }

    private void loadComponentsToPage() {
        Component[] components = {uiComponent.nameInput(), uiComponent.quantityPlusBtn(),
                uiComponent.quantityInput(), uiComponent.quantityMinusBtn(), addItemBtn(), saveBtn()};

        shoppingListPage.addComponents(components);
    }

    private void putTheSavedItemsOnPage() {
        putTheSavedItemsOnPage(shopService.findAllItems());
    }

    private void putTheSavedItemsOnPage(List<Item> items) {
        for (Item item : items)
            putItemOnPage(item.getName(), item.getStrQuantity());
    }

    private void putItemOnPage(String itemName, String itemQuantity) {
        shoppingListItems.incrementItemCount();
        UiItem uiItem = createUiItemComponent(itemName, itemQuantity);
        refreshPageItemsWith(uiItem);
    }

    private UiItem createUiItemComponent(String name, String quantity) {
        return new UiItem(name, quantity, getItemCount());
    }

    private void refreshPageItemsWith(UiItem uiItem) {
        shoppingListItems.add(uiItem);
        addItemsToPage();
    }

    private void addItemsToPage() {
        for (UiItem item : shoppingListItems.getUiItems()) {
            shoppingListPage.addUiItem(item);
            attachDeleteBtnToItem(item);
        }
    }

    private void attachDeleteBtnToItem(UiItem item) {
        shoppingListPage.addComponent(deleteBtn(item));
    }

    private JButton deleteBtn(UiItem item) {
        JButton button = uiComponent.deleteBtn(item.getTopMargin());
        addActionToDeleteBtn(button, item);
        return button;
    }

    private void addActionToDeleteBtn(JButton button, UiItem item) {
        button.addActionListener(e1 -> {
            shoppingListItems.remove(item);
            shoppingListPage.removeUiItem(item);
            refreshPage();
        });
    }

    private void refreshPage() {
        shoppingListItems.clearItemCount();
        removeResults();
    }

    private void removeResults() {
        removeAllDeleteBtn();
        refreshAllItems();
    }

    private void removeAllDeleteBtn() {
        for (Component component : shoppingListPage.getAllComponents())
            removeDeleteBtn(component);
    }

    void removeDeleteBtn(Component component) {
        if (uiComponent.isDeleteBtn(component))
            shoppingListPage.removeComponent(component);
    }

    private void refreshAllItems() {
        for (UiItem uiItem : shoppingListItems.getUiItems()) {
            shoppingListPage.removeUiItem(uiItem);
            refreshShoppingListPage(uiItem);
        }
    }

    private void refreshShoppingListPage(UiItem shoppingListItem) {
        shoppingListItems.incrementItemCount();
        UiItem newItem = createUiItemComponent(shoppingListItem.getName(), shoppingListItem.getStrQuantity());
        addOneItemToPage(newItem);
        shoppingListItems.update(shoppingListItem, newItem);
    }

    private void addOneItemToPage(UiItem itemUi) {
        shoppingListPage.addUiItem(itemUi);
        attachDeleteBtnToItem(itemUi);
    }

    private JButton addItemBtn() {
        JButton addBtn = uiComponent.addBtn();
        addItemBtnAction(addBtn);
        return addBtn;
    }

    private void addItemBtnAction(JButton addBtn) {
        addBtn.addActionListener(e -> {
            if (!uiComponent.isValidItem())
                shoppingListPage.throwAlert(Alert.error(uiComponent.getErrorMessage()));
            else
                refreshPageWhenClickAddBtn();
        });
    }

    private void refreshPageWhenClickAddBtn() {
        putItemOnPage(uiComponent.getItemName(), uiComponent.getItemQuantity());
        clearAlertAndItemsInput();
    }

    private void clearAlertAndItemsInput() {
        shoppingListPage.clearAlert();
        uiComponent.clearItemsInput();
    }

    private JButton saveBtn() {
        JButton saveBtn = uiComponent.saveBtn();
        saveBtn.addActionListener(e -> saveItems());
        return saveBtn;
    }

    private void saveItems() {
        Component alertBox = Alert.success(Message.SAVED_SUCCESSFULLY);
        try {
            shopService.saveItems(shoppingListItems.getItems());
        } catch (ItemNameException | ItemQuantityException e) {
            alertBox = Alert.error(e.getMessage());
        } finally {
            shoppingListPage.throwAlert(alertBox);
        }
    }

    private int getItemCount() {
        return shoppingListItems.getItemCount();
    }
}