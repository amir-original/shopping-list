package shop.frontend;

import shop.Item;

import java.util.LinkedList;
import java.util.List;

public class ShoppingListItems {
    private final List<UiItem> items = new LinkedList<>();
    private int itemCount;

    List<UiItem> getUiItems() {
        return items;
    }

    void add(UiItem item) {
        items.add(item);
    }

    void remove(UiItem item) {
        items.remove(item);
    }

    void update(UiItem src, UiItem dst) {
        items.set(items.indexOf(src), dst);
    }

    List<Item> getItems() {
        List<Item> shopItems = new LinkedList<>();
        for (UiItem uiItem : items)
            shopItems.add(new Item(uiItem.getName(), uiItem.getQuantity()));

        return shopItems;
    }

    public int getItemCount() {
        return itemCount;
    }

    void incrementItemCount() {
        itemCount++;
    }

    void clearItemCount() {
        itemCount = 0;
    }
}
