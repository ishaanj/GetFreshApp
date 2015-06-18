package getfresh.com.getfreshapplication;

/**
 * Created by Ishaan on 6/18/2015.
 */
public class Cart {

    public String getItemName() {
        return itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    private String itemName;
    private String itemPrice;
    private int itemQuantity;

    public Cart(String itemName, String itemPrice, int itemQuantity) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
    }


    @Override
    public String toString() {
        return "Cart{" +
                "itemName='" + itemName + '\'' +
                ", itemPrice='" + itemPrice + '\'' +
                ", itemQuantity=" + itemQuantity +
                '}';
    }
}
