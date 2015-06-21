package getfresh.com.getfreshapplication.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import getfresh.com.getfreshapplication.R;
import getfresh.com.getfreshapplication.data.Cart;
import getfresh.com.getfreshapplication.data.EmailMessage;
import getfresh.com.getfreshapplication.data.UserLocationManager;
import getfresh.com.getfreshapplication.settings.SettingsActivity;

/**
 * Created by Ishaan on 6/19/2015.
 * @author Ishaan
 * @author Somshubra
 */
public class CartFragment extends Fragment implements UserLocationManager.UserLocationManagerListener{

    private static final String PHONE_NO = SettingsActivity.GetFreshPreferenceFragment.KEY_PHONE;
    private static final String USERNAME = SettingsActivity.GetFreshPreferenceFragment.KEY_NAME;
    private static final String ADDRESS = SettingsActivity.GetFreshPreferenceFragment.KEY_ADDRESS;
    public  CartFragment() {  }

    private Button itemTotalText;
    private UserLocationManager manager;
    private AlertDialog alert;
    private boolean isDiscounted = false;
    private int discountCode;

    private ListView lv;
    private CartArrayAdapter adapter;
    private ArrayList<Cart> cList;
    private CartFragmentListener listener;

    private double totalResult;
    private ProgressDialog pd;

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

        try {
            listener = (CartFragmentListener) activity;
        } catch (ClassCastException e) {
            Log.e("CartFragment", "Activity must implement the CartFragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cart, container, false);

        itemTotalText = (Button) v.findViewById(R.id.cart_total);
        if(isDiscounted) {
            itemTotalText.setText("Total (Discount) \u20B9 " + totalResult);
        }
        else {
            itemTotalText.setText("Total \u20B9 " + totalResult);
        }


        itemTotalText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clicking on the Text View to get to here. Send email from here.
                manager  = new UserLocationManager(getActivity());
                manager.setListener(CartFragment.this);

                if(!manager.checkIfGPSIsEnabled()) {
                    alert = manager.alertUserToEnableGPS();
                    alert.show();
                }
                else {
                    manager.getLastKnownLocation();
                }
            }
        });

        lv = (ListView) v.findViewById(R.id.cart_list);
        adapter = new CartArrayAdapter(getActivity(), cList);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Cart c = adapter.getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Remove from Cart")
                        .setMessage("Do you wish to remove " + c.getItemName() + " from the cart?")
                        .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cList.remove(c);
                                adapter.setCartList(cList);

                                if(listener != null)
                                    listener.cartListUpdated(cList);
                            }
                        })
                        .setNegativeButton("Don't Remove", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        lv.setAdapter(adapter);

        return v;
    }

    public void setIsDiscounted(boolean isDiscounted) {
        this.isDiscounted = isDiscounted;
    }

    public void setDiscountCode(int discountCode) {
        this.discountCode = discountCode;
    }

    @Override
    public void onStop() {
        super.onStop();
        if(pd != null) {
            pd.hide();
            pd = null;
        }
    }

    @Override
    public void onDestroy() {
        if(alert != null)
            alert.dismiss();

        if(pd != null) {
            pd.hide();
            pd = null;
        }

        super.onDestroy();
    }

    @Override
    public void gpsDisabledWhenRequestingLastKnownLocation() {
        //TODO: Do something here
        alert = manager.alertUserToEnableGPS();
        alert.show();
    }

    private String no;
    private String name;
    private String address;

    @Override
    public void addressObtained(final List<Address> list, final Location location) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        name = sp.getString(USERNAME, "");
        address = sp.getString(ADDRESS, "");
        no = sp.getString(PHONE_NO, "");

        final EmailMessage.Builder builder = new EmailMessage.Builder(list.get(0))
                .setName(name)
                .setPhoneNumber(no)
                .setAddressLine(address)
                .setTotalPrice(totalResult + "")
                .setSubject()
                .setCartList(getcList())
                .setLat(location.getLatitude() + "")
                .setLon(location.getLongitude() + "")
                .setFeatureName()
                .setPostalCode()
                .setLocality()
                .setSubLocality()
                .setIsDiscounted(isDiscounted?"true" : "false")
                .setDiscountCode(discountCode + "");

        Intent x = builder.build().createEmailIntent();
        getActivity().startActivity(x);
    }

    public void destroyProgressDialog() {
        if(pd != null) {
            pd.hide();
            pd = null;
        }
    }

    @Override
    public void searchingForLocation() {
        if(getActivity() != null)
            pd = ProgressDialog.show(getActivity(), "Getting the current location", "Please wait while we get your location", true);
    }

    @Override
    public void searchForLocationEnded() {
        if(pd != null) {
            pd.hide();
            pd = null;
        }
    }

    private class CartArrayAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private ArrayList<Cart> cartList;

        public CartArrayAdapter(Context context, ArrayList<Cart> cartlist) {
            this.cartList = cartlist;
            inflater = LayoutInflater.from(context);
        }

        public void setCartList(ArrayList<Cart> cartList) {
            this.cartList = cartList;
            notifyDataSetChanged();
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

    public interface CartFragmentListener {
        void cartListUpdated(ArrayList<Cart> list);
    }

}
