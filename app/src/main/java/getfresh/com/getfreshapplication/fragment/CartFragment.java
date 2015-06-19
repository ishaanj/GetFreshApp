package getfresh.com.getfreshapplication.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import getfresh.com.getfreshapplication.R;
import getfresh.com.getfreshapplication.data.Cart;

/**
 * Created by Ishaan on 6/19/2015.
 */
public class CartFragment extends Fragment {

    public CartFragment() {  }

    private ListView lv;
    private TextView itemTotalText;

    private CartArrayAdapter adapter;
    private ArrayList<Cart> cList;

    private double totalResult;

    public void setTotalResult(double totelResult) { this.totalResult = totelResult; }
    public ArrayList<Cart> getcList() {
        return cList;
    }
    public void setcList(ArrayList<Cart> cList) {
        this.cList = cList;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cart, container, false);

        itemTotalText = (TextView) v.findViewById(R.id.cart_total);
        itemTotalText.setText("Total \u20B9 " + totalResult);

        lv = (ListView) v.findViewById(R.id.cart_list);
        adapter = new CartArrayAdapter(getActivity(), cList);

        lv.setAdapter(adapter);

        return v;
    }

    private class CartArrayAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private ArrayList<Cart> cartList;

        public CartArrayAdapter(Context context, ArrayList<Cart> cartlist) {
            this.cartList = cartlist;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if(cartList != null)
                return cartList.size();
            else
                return 0;
        }

        @Override
        public Cart getItem(int position) {
            return cartList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v;
            ViewHolder vh;
            Cart c = getItem(position);

            String name = c.getItemName();
            int quantity = c.getItemQuantity();
            String price = c.getItemPrice();

            if(convertView == null){
                v = inflater.inflate(R.layout.fragment_cart_list_item, parent, false);
                vh = new ViewHolder(v);
            }

            else{
                v = convertView;
                vh = (ViewHolder) v.getTag();
            }
            vh.title.setText(name);
            vh.quantity.setText("x"+Integer.toString(quantity));
            vh.price.setText(Double.toString(Double.parseDouble(price) * quantity));

            return v;
        }

        private class ViewHolder {
            TextView title, quantity, price;

            public ViewHolder(View v) {
                title = (TextView) v.findViewById(R.id.cart_item_title);
                quantity = (TextView) v.findViewById(R.id.cart_item_quantity);
                price = (TextView) v.findViewById(R.id.cart_item_price);

                v.setTag(this);
            }
        }
    }

}
