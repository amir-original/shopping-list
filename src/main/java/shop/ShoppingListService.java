package shop;

import java.util.List;

public interface ShoppingListService {
    List<Item> findAllItems();

    void saveItems(List<Item> items);
}
