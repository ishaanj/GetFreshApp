package getfresh.com.getfreshapplication.fragment;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    private ItemAdapter adapter;

    public MainActivityFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        lv = (ListView) v.findViewById(R.id.main_list);
        adapter = new ItemAdapter();

        lv.setAdapter(adapter);

        return v;
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
        private LayoutInflater inflater;

        public ItemAdapter() {
            inflater = LayoutInflater.from(getActivity());
            itemTitles = getActivity().getResources().getStringArray(R.array.item_titles);
            itemDescriptions = getActivity().getResources().getStringArray(R.array.item_descriptions);
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

            //Bitmap bit = decodeSampledBitmapFromResource(getActivity().getResources(), imageIds[position], vh.img.getWidth(), vh.img.getHeight());
            vh.img.setImageResource(imageIds[position]);

            vh.title.setText(itemTitles[position]);
            vh.desc.setText(itemDescriptions[position]);

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

            public ViewHolder(View v) {
                img = (ImageView) v.findViewById(R.id.list_main_image);
                title = (TextView) v.findViewById(R.id.list_main_title);
                desc = (TextView) v.findViewById(R.id.list_main_description);

                v.setTag(this);
            }
        }
    }


}
