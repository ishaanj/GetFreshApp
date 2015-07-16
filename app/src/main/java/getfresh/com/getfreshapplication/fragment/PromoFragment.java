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
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import getfresh.com.getfreshapplication.R;
import getfresh.com.getfreshapplication.data.Cart;

/**
 * Created by Ishaan on 6/21/2015.
 * @author Ishaan
 * @author Somshubra
 */
public class PromoFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static int CODE_APPLIED = -1;
    private ListView lv;
    private PromoArrayAdapter promoArrayAdapter;
    private ArrayList<Cart> cartArrayList;
    private double cartTotal;
    private int code;
    private int noOfItems;

    public void setCartArrayList(ArrayList<Cart> cartArrayList, double total) {
        this.cartArrayList = cartArrayList;
        cartTotal = total;
        noOfItems = 0;

        for(int i = 0; i < cartArrayList.size(); i++) {
            noOfItems += cartArrayList.get(i).getItemQuantity();
        }
    }

    public double getCartTotal() {
        return cartTotal;
    }

    public void setCartTotal(double cartTotal) {
        this.cartTotal = cartTotal;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
    public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {
        if (CODE_APPLIED == -1) {
            if(noOfItems == 0) {
                Snackbar.make(v, "Add at least 1 item to the cart", Snackbar.LENGTH_LONG).show();
                return;
            }

            code = promoArrayAdapter.getItem(position);
            Snackbar s = validatePromo(v, position);
            if(s != null)
                s.show();
        }
        else {
            if(noOfItems == 0) {
                Snackbar.make(v, "Add at least 1 item to the cart", Snackbar.LENGTH_LONG).show();
                return;
            }

            final String codeName = promoArrayAdapter.getPromo_desc()[position];

            if (CODE_APPLIED == position)
                Snackbar.make(v, "Code " + codeName + " already applied", Snackbar.LENGTH_SHORT).show();
            else {
                Snackbar.make(v, "Replace " + codeName + " with " + promoArrayAdapter.getPromo_desc()[position] + "?", Snackbar.LENGTH_LONG)
                        .setAction("Yes", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Snackbar s = validatePromo(v, position);
                                if(s != null)
                                    s.show();
                            }
                        }).show();
            }
        }

    }

    public Snackbar validatePromo(View v) {
        return validatePromo(v, CODE_APPLIED);
    }

    private Snackbar validatePromo(View v, int position) {
        if (position != -1) {
            final String codeName = promoArrayAdapter.getPromo_desc()[position];

            if (position == 0) {
                if (noOfItems >= 7) {
                    cartTotal = cartTotal - 280;
                    CODE_APPLIED = position;
                    return Snackbar.make(v, "Code " + codeName + " applied", Snackbar.LENGTH_SHORT);
                } else if (noOfItems == 6)
                    return Snackbar.make(v, "Add 1 more item to cart", Snackbar.LENGTH_SHORT);
                else if (noOfItems == 5)
                    return Snackbar.make(v, "Add 2 more items to your cart", Snackbar.LENGTH_SHORT);
                else if (noOfItems < 5)
                    return Snackbar.make(v, "Cart does not have 5 items", Snackbar.LENGTH_LONG);

            } else if (position == 1) {
                if (noOfItems >= 3) {
                    cartTotal = cartTotal * 0.75;
                    CODE_APPLIED = position;
                    return Snackbar.make(v, "Code " + codeName + " applied", Snackbar.LENGTH_SHORT);
                } else
                    return Snackbar.make(v, "Cart does not have 3 items", Snackbar.LENGTH_LONG);
            } else {
                return null;
            }
        } else {
            return null;
        }
        return null;
    }


    private class PromoArrayAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        String[] promo_desc,promo_title;
        int promoImageIds[] = new int[]{
                R.drawable.promo1,
                R.drawable.promo2
        };

        public PromoArrayAdapter(Context context) {
            inflater = LayoutInflater.from(context);
            promo_desc = getActivity().getResources().getStringArray(R.array.promo_desc);
            promo_title = getActivity().getResources().getStringArray(R.array.promo_code);
        }

        public String[] getPromo_desc() {
            return promo_desc;
        }

        @Override
        public int getCount() {
            return promo_title.length;
        }

        @Override
        public Integer getItem(int position) {
            return position;
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

            vh.iw.setImageResource(promoImageIds[position]);

            return v;
        }

        private class ViewHolder {
            ImageView iw;

            public ViewHolder(View v) {
                iw = (ImageView) v.findViewById(R.id.promo_imageView);

                v.setTag(this);
            }
        }
    }

    public int getCodeApplied() {
        return CODE_APPLIED + 1;
    }


}

