package shop;

import java.util.List;

public class ShoppingListServiceImpl implements ShoppingListService{

    private final ShoppingListDAO shoppingListDAO;

    // Dependency Injection
    public ShoppingListServiceImpl(ShoppingListDAO shoppingListDAO) {
        this.shoppingListDAO = shoppingListDAO;
    }

    @Override
    public List<Item> findAllItems() {
        // Get all items from database : Any logic
        return shoppingListDAO.findAllItems();
    }

    @Override
    public void saveItems(List<Item> items) {
        for (Item item : items) {
            item.check();
        }
        // save all items
        shoppingListDAO.saveItems(items);
    }
}
