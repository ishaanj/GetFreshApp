package getfresh.com.getfreshapplication.fragment;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import getfresh.com.getfreshapplication.Cart;
import getfresh.com.getfreshapplication.OnNewCartItemAddedListener;
import getfresh.com.getfreshapplication.R;

public class MainActivityFragment extends Fragment implements AdapterView.OnItemClickListener{
    private ListView lv;
    private boolean isLand;
    private ItemAdapter adapter;
    private OnNewCartItemAddedListener onNewCartItemAddedListener;
    private ArrayList<Cart> cart = new ArrayList<Cart>();

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

        try{
            onNewCartItemAddedListener = (OnNewCartItemAddedListener)getActivity();
        }
        catch (ClassCastException e){
            System.out.println(e);
        }

        Toast.makeText(getActivity(),"Click on item to add to cart",Toast.LENGTH_SHORT).show();

        return v;
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

        cart.add(adapter.getItem(position));

        for(Cart c:cart)
        System.out.println(c);
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

            Cart temp = new Cart(name,price,quantity);
            return temp;
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

        public Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(res, resId, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeResource(res, resId, options);
        }

        public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) > reqHeight
                        && (halfWidth / inSampleSize) > reqWidth) {
                    inSampleSize *= 2;
                }
            }

            return inSampleSize;
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
}
