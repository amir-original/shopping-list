package shop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

// Acceptance Test
public class ItemTest {

    private ShoppingListService shoppingList;
    private List<Item> items;

    @BeforeEach
    void setUp() {
        shoppingList = new ShoppingListServiceImpl(new ShoppingListDAOImplMock());
        items = new LinkedList<>();
    }

    @Test
    void check_if_item_names_are_not_null_and_empty() {
        items.add(new Item("Banana",5));
        items.add(new Item("Orange",2));

        assertDoesNotThrow(() -> shoppingList.saveItems(items));
    }

    @Test
    void throw_item_name_Exception_when_an_item_name_is_null() {
        items.add(new Item("Banana",5));
        items.add(new Item(null,2));

        assertThrows(ItemNameException.class,()-> shoppingList.saveItems(items));
    }

    @Test
    void throw_item_name_Exception_when_an_item_name_is_empty() {
        items.add(new Item("",5));
        items.add(new Item("orange",2));

        assertThrows(ItemNameException.class,()-> shoppingList.saveItems(items));
    }

    @Test
    void throw_item_quantity_Exception_when_an_item_has_a_quantity_less_than_1() {
        items.add(new Item("Banana",0));
        items.add(new Item("Orange",2));

        assertThrows(ItemQuantityException.class,()-> shoppingList.saveItems(items));
    }

    @Test
    void throw_item_quantity_Exception_when_an_item_has_a_quantity_greater_than_100() {
        items.add(new Item("Banana",15));
        items.add(new Item("Orange",101));

        assertThrows(ItemQuantityException.class,()-> shoppingList.saveItems(items));
    }

    static class ShoppingListDAOImplMock implements ShoppingListDAO{

        @Override
        public List<Item> findAllItems() {
            return null;
        }

        @Override
        public void saveItems(List<Item> items) {

        }
    }
}
