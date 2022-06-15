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
        return shoppingListDAO.findAllItems();
    }

    @Override
    public void saveItems(List<Item> items) {
        for (Item item : items) {
            item.check();
        }
        shoppingListDAO.saveItems(items);
    }
}
