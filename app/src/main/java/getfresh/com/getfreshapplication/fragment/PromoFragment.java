package getfresh.com.getfreshapplication.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import getfresh.com.getfreshapplication.R;
import getfresh.com.getfreshapplication.data.Cart;

/**
 * Created by Ishaan on 6/21/2015.
 */
public class PromoFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static int CODE_APPLIED = 0;
    private ListView lv;
    private PromoArrayAdapter promoArrayAdapter;
    private ArrayList<Cart> cartArrayList;
    private double cartTotal;
    private String code;

    public void setCartArrayList(ArrayList<Cart> cartArrayList,double total) {
        this.cartArrayList = cartArrayList;
        cartTotal = total;
    }


    public double getCartTotal() {
        return cartTotal;
    }

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
        lv.setOnItemClickListener(this);

        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        if (CODE_APPLIED == 0){

            code = promoArrayAdapter.getItem(position);
            if(code.equalsIgnoreCase(promoArrayAdapter.getItem(0)))
            {
                cartTotal = cartTotal * 90 / 100;
                CODE_APPLIED++;
                Snackbar.make(view,"Code "+code+" applied",Snackbar.LENGTH_SHORT).show();
            }
            else if(code.equalsIgnoreCase(promoArrayAdapter.getItem(1)))
            {
                if (cartArrayList.size() > 5) {
                    cartTotal = cartTotal * 80 / 100;
                    CODE_APPLIED++;
                    Snackbar.make(view,"Code "+code+" applied",Snackbar.LENGTH_SHORT).show();
                }
                else
                    Snackbar.make(view,"Cart does not have 5 items",Snackbar.LENGTH_LONG).show();
            }

        }

        else if(CODE_APPLIED == 1){
            if(code.equalsIgnoreCase(promoArrayAdapter.getItem(position)))
            Snackbar.make(view,"Code "+code+" already applied",Snackbar.LENGTH_SHORT).show();

            else {
                Snackbar.make(view, "Replace " + code + " with " + promoArrayAdapter.getItem(position) + "?", Snackbar.LENGTH_LONG)
                        .setAction("Yes", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                code = promoArrayAdapter.getItem(position);
                                if(code.equalsIgnoreCase(promoArrayAdapter.getItem(0)))
                                {
                                    cartTotal = cartTotal * 90 / 100;
                                    CODE_APPLIED++;
                                    Snackbar.make(v,"Code "+code+" applied",Snackbar.LENGTH_SHORT).show();
                                }
                                else if(code.equalsIgnoreCase(promoArrayAdapter.getItem(1)))
                                {
                                    if (cartArrayList.size() > 5) {
                                        cartTotal = cartTotal * 80 / 100;
                                        CODE_APPLIED++;
                                        Snackbar.make(v,"Code "+code+" applied",Snackbar.LENGTH_SHORT).show();
                                    }
                                    else
                                        Snackbar.make(v,"Cart does not have 5 items",Snackbar.LENGTH_LONG).show();
                                }
                            }
                        }).show();
            }
        }
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
