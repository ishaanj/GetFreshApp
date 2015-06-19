package getfresh.com.getfreshapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import getfresh.com.getfreshapplication.data.Cart;

public class CartArrayAdapter extends ArrayAdapter<Cart> {

    int resource;

    public CartArrayAdapter(Context context, int resource, ArrayList<Cart> cart) {
        super(context, resource, cart);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout cView;

        Cart c = getItem(position);

        String name = c.getItemName();
        int quantity = c.getItemQuantity();
        String price = c.getItemPrice();

        if(convertView == null){
            cView = new RelativeLayout(getContext());

            String inflater = Context.LAYOUT_INFLATER_SERVICE;

            LayoutInflater li;

            li = (LayoutInflater)getContext().getSystemService(inflater);
            li.inflate(resource, cView, true);

        }

        else{
            cView = (RelativeLayout)convertView;
        }

        TextView titleView = (TextView)cView.findViewById(R.id.cart_item_title);
        TextView quantityView = (TextView)cView.findViewById(R.id.cart_item_quantity);
        TextView priceView = (TextView)cView.findViewById(R.id.cart_item_price);

        titleView.setText(name);
        quantityView.setText("x"+Integer.toString(quantity));
        priceView.setText(Double.toString(Double.parseDouble(price)*quantity));

        return cView;
    }


}
