package getfresh.com.getfreshapplication.data;

/**
 * Created by Ishaan on 6/18/2015.
 * @author Ishaan
 * @author Somshubra
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

    public Cart(String itemName, String itemPrice) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    @Override
    public boolean equals(Object o) {
        Cart c = (Cart) o;
        return this.itemName.equals(c.itemName);
    }

    @Override
    public String toString() {
        return itemName + "\tQuantity = " + itemQuantity + "\tPrice = " + (itemQuantity * Double.parseDouble(itemPrice));
    }
}
