package shop;

public class ItemQuantityException extends RuntimeException {
    public ItemQuantityException() {
        super("item quantity shouldn't less than 1 or greater than 100");
    }
}
