package getfresh.com.getfreshapplication.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import getfresh.com.getfreshapplication.R;
import getfresh.com.getfreshapplication.data.Cart;

/**
 * @author Somshubra
 */
public class MainActivityFragment extends Fragment implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {
    public static final String TAG = "MainActivityFragment";
    private ListView lv;
    private boolean isLand;
    private ItemAdapter adapter;
    private ArrayList<Cart> cartList = new ArrayList<Cart>();

    private int[] imageIdBig = new int[] {
            R.drawable.aaismasala_big,
            R.drawable.chillycheese_big,
            R.drawable.comint_big,
            R.drawable.creamyajwain_big,
            R.drawable.freshthai_big,
            R.drawable.jaffana_big,
            R.drawable.kasurimethi_big,
            R.drawable.kolkatacalling_big,
            R.drawable.kovalam_big,
            R.drawable.madrasmagic_big,
            R.drawable.nizamekhas_big,
            R.drawable.peppybbq_big,
            R.drawable.periperi_big,
            R.drawable.rechado_big,
            R.drawable.suriyani_big,
            R.drawable.tikkatwist_big
    };

    private MainActivityFragmentListener listener;

    public MainActivityFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        adapter = new ItemAdapter();

        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Instructions");
        final TextView input = new TextView(getActivity());
        input.setText("Click on item to add it to your cart \nClick and hold down on an item to know more about it");
        input.setLeft(15);
        alert.setView(input);
        alert.setNegativeButton("Got It", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        alert.show();

        lv = (ListView) v.findViewById(R.id.main_list);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(this);
        lv.setOnItemLongClickListener(this);
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener = (MainActivityFragmentListener) activity;
        } catch(ClassCastException e) {
            //TODO: Remove logs before deploy
            Log.e(TAG, "Actitivy must implement OnNewCardOtemAddedListener ");
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //String name = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(SettingsActivity.GetFreshPreferenceFragment.KEY_NAME, "");
        //Snackbar.make(lv, "Welcome back, " + name, Snackbar.LENGTH_SHORT).show();
    }

    private boolean isLandscape() {
        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            isLand = true;
        }
        else {
            isLand = false;
        }
        return isLand;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        final Cart temp = adapter.getItem(position);

        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Please enter how many you would like");

        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setRawInputType(Configuration.KEYBOARD_12KEY);
        input.setHint("1, 2, 3...");
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                int count = Integer.parseInt(input.getText().toString());
                if (count >= 0) {
                    temp.setItemQuantity(count);
                    adapter.updateCard(position, temp);

                    if (cartList.contains(temp)) {
                        cartList.remove(temp);
                    }

                    cartList.add(temp);

                    if (listener != null) {
                        listener.onNewCartItemAddedListener(adapter.getItem(position));
                    }
                } else {
                    Snackbar.make(input, "Only Positive numbers!", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,int reqHeight){
        //Raw height and width of the Image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height>reqHeight || width>reqWidth) {
            final int heightratio = Math.round((float)height / (float)reqHeight);
            final int widthRatio = Math.round((float)width / (float)reqWidth);

            inSampleSize = heightratio < widthRatio ? heightratio : widthRatio;
        }
        return inSampleSize;
    }
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,int reqWidth,int reqHeight){
        //first decode with inJustdecodeBounds = true to check dimensions.

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        //Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        //Decode bitmap with inSampleSize
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeResource(res, resId, options);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton("Add to cart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                final Cart temp = adapter.getItem(position);

                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Please enter how many you would like");

                final EditText input = new EditText(getActivity());
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setRawInputType(Configuration.KEYBOARD_12KEY);
                input.setHint("1, 2, 3...");
                alert.setView(input);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        int count = Integer.parseInt(input.getText().toString());
                        if (count >= 0) {
                            temp.setItemQuantity(count);
                            adapter.updateCard(position, temp);

                            if (cartList.contains(temp)) {
                                cartList.remove(temp);
                            }

                            cartList.add(temp);

                            if (listener != null) {
                                listener.onNewCartItemAddedListener(adapter.getItem(position));
                            }
                        } else {
                            Snackbar.make(input, "Only Positive numbers!", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                alert.show();

                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();

        LayoutInflater inflater = adapter.getInflater();
        if(inflater == null) {
            inflater = LayoutInflater.from(getActivity());
        }
        View v = inflater.inflate(R.layout.dialog_item_details, null, false);
        final ImageView im = (ImageView) v.findViewById(R.id.dialog_item_img);

        dialog.setView(v, 0, 0, 0, 0);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Bitmap b = decodeSampledBitmapFromResource(getActivity().getResources(), imageIdBig[position], im.getWidth(), im.getHeight());
                im.setImageBitmap(b);
            }
        });
        dialog.show();
        return true;
    }

    private class ItemAdapter extends BaseAdapter {
        private int[] imageIds = new int[] {
                R.drawable.am,
                R.drawable.cc,
                R.drawable.c,
                R.drawable.ca,
                R.drawable.ft,
                R.drawable.j,
                R.drawable.km,
                R.drawable.kc,
                R.drawable.k,
                R.drawable.mm,
                R.drawable.nek,
                R.drawable.pbbq,
                R.drawable.pp,
                R.drawable.r,
                R.drawable.s,
                R.drawable.tt
        };
        private String[] itemTitles;
        //private String[] itemDescriptions;
        private String[] itemPrices;
        //private String[] itemSubtitles;
        //private String[] itemFlavour1;
        //private String[] itemFlavour2;
        private Cart[] carts;

        private LayoutInflater inflater;

        public ItemAdapter() {
            inflater = LayoutInflater.from(getActivity());
            itemTitles = getActivity().getResources().getStringArray(R.array.item_titles);
            //itemDescriptions = getActivity().getResources().getStringArray(R.array.item_descriptions);
            itemPrices = getActivity().getResources().getStringArray(R.array.item_prices);
            //itemSubtitles = getActivity().getResources().getStringArray(R.array.item_subtitles);
            //itemFlavour1 = getActivity().getResources().getStringArray(R.array.item_flavours1);
            //itemFlavour2 = getActivity().getResources().getStringArray(R.array.item_flavours2);

            carts = new Cart[itemTitles.length];
            for(int i = 0; i < carts.length; i++) {
                carts[i] = new Cart(itemTitles[i], itemPrices[i] + "");
            }
        }

        public LayoutInflater getInflater() {
            return inflater;
        }

        @Override
        public int getCount() {
            // length = 16
            if(itemTitles != null)
                return itemTitles.length;
            else
                return 0;
        }

        @Override
        public Cart getItem(int position) {
            return carts[position];
        }

        public void updateCard(int position, Cart cart) {
            carts[position] = cart;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v;
            ViewHolder vh;

            if(convertView == null) {
                v = inflater.inflate(R.layout.fragment_main_list_item, parent, false);
                vh = new ViewHolder(v);
            }
            else {
                v = convertView;
                vh = (ViewHolder) v.getTag();
            }

            vh.img.setImageResource(imageIds[position]);
            //vh.title.setText(itemTitles[position]);
            //vh.desc.setText(itemDescriptions[position]);
            //vh.price.setText(itemPrices[position]);
            //vh.subtitle.setText(itemSubtitles[position]);

            /*
            if(!itemFlavour2[position].equals(""))
                vh.flavour1.setText(itemFlavour1[position] + "," + itemFlavour2[position]);
            else
                vh.flavour1.setText(itemFlavour1[position]);
            */

            return v;
        }

        private class ViewHolder {
            ImageView img;
            //TextView title;
            //TextView desc;
            //TextView price;
            //TextView subtitle;
            //TextView flavour1;

            public ViewHolder(View v) {
                img = (ImageView) v.findViewById(R.id.list_main_image);
                //title = (TextView) v.findViewById(R.id.list_main_title);
                //desc = (TextView) v.findViewById(R.id.list_main_description);
                //price = (TextView) v.findViewById(R.id.list_main_price);
                //subtitle = (TextView) v.findViewById(R.id.list_main_subtitle);
                //flavour1 = (TextView)v.findViewById(R.id.list_main_flavour1);
                v.setTag(this);
            }
        }
    }

    public interface MainActivityFragmentListener {
        void onNewCartItemAddedListener(Cart cart);
    }

    public ArrayList<Cart> getCartList() {
        return cartList;
    }

    public void setCartList(ArrayList<Cart> cart) {
        this.cartList = cart;
    }
}
