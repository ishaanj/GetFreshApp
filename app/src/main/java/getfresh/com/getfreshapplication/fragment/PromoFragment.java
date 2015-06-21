package getfresh.com.getfreshapplication.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import getfresh.com.getfreshapplication.R;

/**
 * Created by Ishaan on 6/21/2015.
 */
public class PromoFragment extends Fragment {

    private static int CODE_APPLIED = 0;
    private ListView lv;
    private PromoArrayAdapter promoArrayAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_promo, container, false);

        lv = (ListView) v.findViewById(R.id.promo_list);
        promoArrayAdapter = new PromoArrayAdapter(getActivity());

        lv.setAdapter(promoArrayAdapter);

        return v;
    }


    private class PromoArrayAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private String[] promo_desc;
        private String[] promo_title;

        public PromoArrayAdapter(Context context) {
            inflater = LayoutInflater.from(context);
            promo_desc = getActivity().getResources().getStringArray(R.array.promo_desc);
            promo_title = getActivity().getResources().getStringArray(R.array.promo_code);
        }

        @Override
        public int getCount() {
            return promo_title.length;
        }

        @Override
        public String getItem(int position) {
            return promo_title[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v;
            ViewHolder vh;

            if (convertView == null) {
                v = inflater.inflate(R.layout.fragment_promo_list, parent, false);
                vh = new ViewHolder(v);
            } else {
                v = convertView;
                vh = (ViewHolder) v.getTag();
            }

            vh.title.setText(promo_title[position]);
            vh.desc.setText(promo_desc[position]);

            return v;
        }

        private class ViewHolder {
            TextView title, desc;

            public ViewHolder(View v) {
                title = (TextView) v.findViewById(R.id.promo_item_title);
                desc = (TextView) v.findViewById(R.id.promo_item_desc);

                v.setTag(this);
            }
        }
    }
}
