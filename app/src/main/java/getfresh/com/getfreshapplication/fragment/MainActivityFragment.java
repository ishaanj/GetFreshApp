package getfresh.com.getfreshapplication.fragment;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import getfresh.com.getfreshapplication.R;
import getfresh.com.getfreshapplication.data.Cart;

public class MainActivityFragment extends Fragment implements AdapterView.OnItemClickListener{
    public static final String TAG = "MainActivityFragment";
    private ListView lv;
    private boolean isLand;
    private ItemAdapter adapter;
    private ArrayList<Cart> cartList = new ArrayList<Cart>();

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

        lv = (ListView) v.findViewById(R.id.main_list);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(this);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        final Snackbar snackbar =  Snackbar.make(lv, "Click on item to add to cart", Snackbar.LENGTH_LONG);
        snackbar.setAction("Dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        }).show();
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        cartList.add(adapter.getItem(position));

        if(listener != null) {
            listener.onNewCartItemAddedListener(adapter.getItem(position));
        }
    }

    private class ItemAdapter extends BaseAdapter {
        private int[] imageIds = new int[] {
                R.drawable.aaismasala,
                R.drawable.chillycheese,
                R.drawable.comint,
                R.drawable.creamyajwain,
                R.drawable.freshthai,
                R.drawable.jaffna,
                R.drawable.kasurimethi,
                R.drawable.kolkatacalling,
                R.drawable.kovalam,
                R.drawable.madrasmagic,
                R.drawable.nizamekhas,
                R.drawable.peppybbq,
                R.drawable.periperi,
                R.drawable.rechado,
                R.drawable.suriyani,
                R.drawable.tikkatwist
        };
        private String[] itemTitles;
        private String[] itemDescriptions;
        private String[] itemPrices;
        private String[] itemSubtitles;
        private String[] itemFlavour1;
        private String[] itemFlavour2;
        private LayoutInflater inflater;

        public ItemAdapter() {
            inflater = LayoutInflater.from(getActivity());
            itemTitles = getActivity().getResources().getStringArray(R.array.item_titles);
            itemDescriptions = getActivity().getResources().getStringArray(R.array.item_descriptions);
            itemPrices = getActivity().getResources().getStringArray(R.array.item_prices);
            itemSubtitles = getActivity().getResources().getStringArray(R.array.item_subtitles);
            itemFlavour1 = getActivity().getResources().getStringArray(R.array.item_flavours1);
            itemFlavour2 = getActivity().getResources().getStringArray(R.array.item_flavours2);
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

            String name = itemTitles[position];
            String price = itemPrices[position];
            int quantity = 1;

            return new Cart(name, price, quantity);
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
            vh.title.setText(itemTitles[position]);
            vh.desc.setText(itemDescriptions[position]);
            vh.price.setText(itemPrices[position]);
            vh.subtitle.setText(itemSubtitles[position]);

            if(!itemFlavour2[position].equals(""))
                vh.flavour1.setText(itemFlavour1[position] + "," + itemFlavour2[position]);
            else
                vh.flavour1.setText(itemFlavour1[position]);

            return v;
        }

        private class ViewHolder {
            ImageView img;
            TextView title;
            TextView desc;
            TextView price;
            TextView subtitle;
            TextView flavour1;

            public ViewHolder(View v) {
                img = (ImageView) v.findViewById(R.id.list_main_image);
                title = (TextView) v.findViewById(R.id.list_main_title);
                desc = (TextView) v.findViewById(R.id.list_main_description);
                price = (TextView) v.findViewById(R.id.list_main_price);
                subtitle = (TextView) v.findViewById(R.id.list_main_subtitle);
                flavour1 = (TextView)v.findViewById(R.id.list_main_flavour1);
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
