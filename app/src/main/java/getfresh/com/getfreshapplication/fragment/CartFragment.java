package getfresh.com.getfreshapplication.fragment;

import android.Manifest;
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
import android.support.v7.widget.ShareActionProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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

    private static final String NAME = SettingsActivity.GetFreshPreferenceFragment.KEY_NAME;
    private static final String ALT_NAME = SettingsActivity.GetFreshPreferenceFragment.KEY_ALT_NAME;
    private static final String PHONE_NO = SettingsActivity.GetFreshPreferenceFragment.KEY_PHONE;
    private static final String USERNAME = SettingsActivity.GetFreshPreferenceFragment.KEY_NAME;
    private static final String ADDRESS = SettingsActivity.GetFreshPreferenceFragment.KEY_ADDRESS;
    private static final String ADDRESS_STREET = SettingsActivity.GetFreshPreferenceFragment.KEY_ADDRESS_STREET;
    private static final String ADDRESS_BUILDING = SettingsActivity.GetFreshPreferenceFragment.KEY_ADDRESS_BUILDING;
    private static final String ALTERNATE_ADDRESS = SettingsActivity.GetFreshPreferenceFragment.KEY_ALT_ADDRESS;
    private static final String ALTERNATE_STREET = SettingsActivity.GetFreshPreferenceFragment.KEY_ALT_ADDRESS_STREET;
    private static final String ALTERNATE_BUILDING = SettingsActivity.GetFreshPreferenceFragment.KEY_ALT_ADDRESS_BUILDING;

    public  CartFragment() {  }

    private Button checkout;
    private ImageButton share;
    private TextView itemTotalText;
    private TextView itemTotalTaxed;
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
    private EmailMessage.Builder builder;
    private boolean datetimeNow = false;
    private Calendar cal;

    private ShareActionProvider shareActionProvider;

    //addressResultType : 1 for Home, 2 for Alternate
    private int addressResultType = 0;

    public void setTotalResult(double totelResult) { this.totalResult = totelResult; }
    public ArrayList<Cart> getcList() {
        return cList;
    }
    public void setcList(ArrayList<Cart> cList) {
        this.cList = cList;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);

        try {
            listener = (CartFragmentListener) activity;
        } catch (ClassCastException e) {
            Log.e("CartFragment", "Activity must implement the CartFragmentListener");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //getActivity().getMenuInflater().inflate(R.menu.menu_cart, menu);
        //MenuItem shareItem = menu.findItem(R.id.cart_share_action);
        //shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cart, container, false);

        checkout = (Button)v.findViewById(R.id.checkout);
        itemTotalText = (TextView) v.findViewById(R.id.cart_price_total);
        itemTotalTaxed = (TextView) v.findViewById(R.id.taxed_total);
        share = (ImageButton)v.findViewById(R.id.shareButton);

        if(isDiscounted) {
            itemTotalText.setText("Total (Discount) : ₹" + totalResult);
        }
        else {
            itemTotalText.setText("Total : ₹" + totalResult);
        }

        itemTotalTaxed.setText("Total (inc. 5% VAT) : ₹"+ Math.ceil(totalResult * 1.05));

        share.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v) {
                final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String message = "";

                message += "Delivery Date : " + new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
                message += "\nParty: " + sp.getString(NAME, "");
                message += "\nLocation: " + sp.getString(ADDRESS, "");

                if(cList != null) {
                    for(Cart c : cList){
                       message += "\n" + c.toStringNoPrice();
                    }

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                    sendIntent.setType("text/plain");
                    sendIntent.setPackage("com.whatsapp");
                    startActivity(sendIntent);
                }
            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                AlertDialog.Builder requestAddress = new AlertDialog.Builder(getActivity());
                requestAddress.setTitle("Select Address");

                String array[] = new String[]{"Home Address", "Another Address", "New Alternate Address"};
                // Load all addresses and location data stored in shared preferenced
                final String haddress = sp.getString(ADDRESS, "");
                final String aladdress = sp.getString(ALTERNATE_ADDRESS, "");

                final String hBuilding = sp.getString(ADDRESS_BUILDING, "");
                final String hStreet = sp.getString(ADDRESS_STREET, "");

                final String alBuilding = sp.getString(ALTERNATE_BUILDING, "");
                final String alStreet = sp.getString(ALTERNATE_STREET, "");

                final boolean hExtra = !TextUtils.isEmpty(hBuilding) && !TextUtils.isEmpty(hStreet);
                final boolean alExtra = !TextUtils.isEmpty(alBuilding) && !TextUtils.isEmpty(alStreet);

                // Check for empty strings and map address to array
                if (!TextUtils.isEmpty(haddress)) {
                    String address = "";
                    if (!TextUtils.isEmpty(hBuilding))
                        address = hBuilding + ", ";
                    if (!TextUtils.isEmpty(hStreet))
                        address += hStreet + ", ";
                    address += haddress;
                    array[0] = address;
                }
                if (!TextUtils.isEmpty(aladdress)) {
                    String address = "";
                    if (!TextUtils.isEmpty(alBuilding))
                        address = alBuilding + ", ";
                    if (!TextUtils.isEmpty(alStreet))
                        address += alStreet + ", ";
                    address += aladdress;
                    array[0] = address;
                }

                requestAddress.setSingleChoiceItems(array, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        if (which == 0) {
                            createHomeAddressDialog(dialog);

                        } else if (which == 1) {
                            if (!TextUtils.isEmpty(aladdress) && alExtra) {
                                // Alternate address
                                addressResultType = 2;

                                createDateTimeDialog().show();
                                dialog.dismiss();
                            } else {
                                createAlternateAlertDialog();
                                dialog.dismiss();
                            }
                        } else {
                            createAlternateAlertDialog();
                            dialog.dismiss();
                        }
                    }

                    private void createHomeAddressDialog(final DialogInterface dialog) {
                        if (!TextUtils.isEmpty(haddress) && hExtra) {
                            addressResultType = 1;

                            createDateTimeDialog().show();
                            dialog.dismiss();
                        } else {
                            AlertDialog.Builder addressBuilder = new AlertDialog.Builder(getActivity());
                            addressBuilder.setTitle("Address Details");

                            // Get inner views
                            View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_name_address, null, false);
                            final EditText editName = (EditText) v.findViewById(R.id.edit_dialog_name);
                            final EditText editAddress = (EditText) v.findViewById(R.id.edit_dialog_address);
                            final EditText editBuilding = (EditText) v.findViewById(R.id.edit_dialog_building);
                            final EditText editStreet = (EditText) v.findViewById(R.id.edit_dialog_street);

                            // Set text in inner rows
                            editName.setText(sp.getString(NAME, ""));
                            editAddress.setText(haddress);
                            editBuilding.setText(hBuilding);
                            editStreet.setText(hStreet);

                            addressBuilder.setView(v);

                            addressBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog2, int which) {
                                    // Get inputs from dialog
                                    String name = editName.getText().toString().trim();
                                    String address = editAddress.getText().toString().trim();
                                    String building = editBuilding.getText().toString().trim();
                                    String street = editStreet.getText().toString().trim();

                                    SharedPreferences.Editor edit = sp.edit();

                                    // Validate and save in shared preferences
                                    if (TextUtils.isEmpty(name)) {
                                        Toast.makeText(getActivity(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
                                        return;
                                    } else {
                                        edit.putString(NAME, name);
                                    }

                                    if (TextUtils.isEmpty(building)) {
                                        Toast.makeText(getActivity(), "Building cannot be empty", Toast.LENGTH_SHORT).show();
                                        return;
                                    } else {
                                        edit.putString(ADDRESS_BUILDING, building);
                                    }

                                    if (TextUtils.isEmpty(street)) {
                                        Toast.makeText(getActivity(), "Street cannot be empty", Toast.LENGTH_SHORT).show();
                                        return;
                                    } else {
                                        edit.putString(ADDRESS_STREET, street);
                                    }

                                    if (TextUtils.isEmpty(address)) {
                                        Toast.makeText(getActivity(), "Address cannot be empty", Toast.LENGTH_SHORT).show();
                                        return;
                                    } else {
                                        edit.putString(ADDRESS, address);
                                    }

                                    edit.commit();

                                    // Home address
                                    addressResultType = 1;

                                    createDateTimeDialog().show();
                                    dialog2.dismiss();
                                    dialog.dismiss();
                                }
                            });

                            addressBuilder.show();
                        }
                    }

                    private void createAlternateAlertDialog() {
                        AlertDialog.Builder addressBuilder = new AlertDialog.Builder(getActivity());
                        addressBuilder.setTitle("Alternate Address Details");

                        // Get inner views
                        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_name_address, null, false);
                        final EditText editName = (EditText) v.findViewById(R.id.edit_dialog_name);
                        final EditText editAddress = (EditText) v.findViewById(R.id.edit_dialog_address);
                        final EditText editBuilding = (EditText) v.findViewById(R.id.edit_dialog_building);
                        final EditText editStreet = (EditText) v.findViewById(R.id.edit_dialog_street);

                        // Set values to inner views
                        editName.setText(sp.getString(ALT_NAME, ""));
                        editAddress.setText(sp.getString(ALTERNATE_ADDRESS, ""));
                        editBuilding.setText(sp.getString(ALTERNATE_BUILDING, ""));
                        editStreet.setText(sp.getString(ALTERNATE_STREET, ""));

                        addressBuilder.setView(v);

                        addressBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Get user input from dialog
                                String name = editName.getText().toString().trim();
                                String address = editAddress.getText().toString().trim();
                                String building = editBuilding.getText().toString().trim();
                                String street = editStreet.getText().toString().trim();

                                SharedPreferences.Editor edit = sp.edit();

                                // Validate and save in shared preferences
                                if (TextUtils.isEmpty(name)) {
                                    Toast.makeText(getActivity(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
                                    return;
                                } else {
                                    edit.putString(ALT_NAME, name);
                                }

                                if (TextUtils.isEmpty(building)) {
                                    Toast.makeText(getActivity(), "Building cannot be empty", Toast.LENGTH_SHORT).show();
                                    return;
                                } else {
                                    edit.putString(ALTERNATE_BUILDING, building);
                                }

                                if (TextUtils.isEmpty(street)) {
                                    Toast.makeText(getActivity(), "Street cannot be empty", Toast.LENGTH_SHORT).show();
                                    return;
                                } else {
                                    edit.putString(ALTERNATE_STREET, street);
                                }

                                if (TextUtils.isEmpty(address)) {
                                    Toast.makeText(getActivity(), "Address cannot be empty", Toast.LENGTH_SHORT).show();
                                    return;
                                } else {
                                    edit.putString(ALTERNATE_ADDRESS, address);
                                }

                                edit.commit();

                                // Alternate address
                                addressResultType = 2;

                                createDateTimeDialog().show();
                                dialog.dismiss();
                            }
                        });

                        addressBuilder.show();
                    }
                });

                requestAddress.show();
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

    private AlertDialog.Builder createDateTimeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Date/Time to Deliver");

        final String choices[] = new String[] {"Deliver Today", "Deliver On ..."};

        builder.setSingleChoiceItems(choices, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int ch) {
                if(ch == 0) {
                    datetimeNow = true;
                    /**
                    * Actual call to get the location */
                    manager  = new UserLocationManager(getActivity());
                    manager.setListener(CartFragment.this);
                    String[] loc = {Manifest.permission.ACCESS_FINE_LOCATION};
                    requestPermissions(loc, 1340);
                    if(!manager.checkIfGPSIsEnabled()) {

                        alert = manager.alertUserToEnableGPS();
                        alert.show();
                    }
                    else {
                        manager.getLastKnownLocation();
                    }
                    dialog.dismiss();
                }
                else {
                    datetimeNow = false;
                    cal = Calendar.getInstance();
                    DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
                            cal.set(year, month, day);

                            TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(RadialPickerLayout radialPickerLayout, int i, int i1) {
                                    cal.set(Calendar.HOUR_OF_DAY, i);
                                    cal.set(Calendar.MINUTE, i1);

                                    /**
                                     * Actual call to get the location
                                     */
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
                            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true)
                                    .show(getActivity().getFragmentManager(), "timePicker");

                            datePickerDialog.dismiss();
                        }
                    }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
                            .show(getActivity().getFragmentManager(), "datePicker");

                    dialog.dismiss();
                }
            }
        });

        return builder;
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
    private String address, building, street;
    private String date, time;

    @Override
    public void addressObtained(final List<Address> list, final Location location) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if(addressResultType == 1) {
            name = sp.getString(USERNAME, "");
            address = sp.getString(ADDRESS, "");
            building = sp.getString(ADDRESS_BUILDING, "");
            street = sp.getString(ADDRESS_STREET, "");
        }
        else if(addressResultType == 2) {
            name = sp.getString(ALT_NAME, "");
            address = sp.getString(ALTERNATE_ADDRESS, "");
            building = sp.getString(ALTERNATE_BUILDING, "");
            street = sp.getString(ALTERNATE_STREET, "");
        }

        no = sp.getString(PHONE_NO, "");

        builder = new EmailMessage.Builder(list.get(0))
                .setName(name)
                .setPhoneNumber(no)
                .setAddressLine(address, building, street)
                .setTotalPrice(totalResult + "")
                .setSubject()
                .setCartList(getcList())
                .setLat(location.getLatitude() + "")
                .setLon(location.getLongitude() + "")
                .setFeatureName()
                .setPostalCode()
                .setLocality()
                .setSubLocality()
                .setIsDiscounted(isDiscounted? "true" : "false")
                .setDiscountCode(discountCode + "");


        if(!datetimeNow) {
            DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
            SimpleDateFormat tf = new SimpleDateFormat("HH:mm", Locale.getDefault());

            date = df.format(cal.getTime());
            time = tf.format(cal.getTime());

            builder.setDate(date).setTime(time);
        }

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
