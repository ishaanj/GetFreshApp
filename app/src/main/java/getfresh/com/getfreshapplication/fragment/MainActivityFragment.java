package getfresh.com.getfreshapplication.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import getfresh.com.getfreshapplication.R;

public class MainActivityFragment extends Fragment {
    private ListView lv;
    private boolean isLand;

    private ItemAdapter adapter;

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
        private LayoutInflater inflater;

        public ItemAdapter() {
            inflater = LayoutInflater.from(getActivity());
            itemTitles = getActivity().getResources().getStringArray(R.array.item_titles);
            itemDescriptions = getActivity().getResources().getStringArray(R.array.item_descriptions);
            itemPrices = getActivity().getResources().getStringArray(R.array.item_prices);
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
        public Object getItem(int position) {
            return null;
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

            return v;
        }

        private class ViewHolder {
            ImageView img;
            TextView title;
            TextView desc;
            TextView price;

            public ViewHolder(View v) {
                img = (ImageView) v.findViewById(R.id.list_main_image);
                title = (TextView) v.findViewById(R.id.list_main_title);
                desc = (TextView) v.findViewById(R.id.list_main_description);
                price = (TextView) v.findViewById(R.id.list_main_price);

                v.setTag(this);
            }
        }
    }
}
