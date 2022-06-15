package shop;

public class ItemNameException extends RuntimeException {

    public ItemNameException(){
        super("item name shouldn't empty");
    }
}
