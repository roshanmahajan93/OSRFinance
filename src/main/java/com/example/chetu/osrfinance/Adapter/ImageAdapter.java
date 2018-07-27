package com.example.chetu.osrfinance.Adapter;

/**
 * Created by Narendra on 11/3/2014.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chetu.osrfinance.R;


public class ImageAdapter extends BaseAdapter {
    private Context context;
    private final String[] mobileValues;

    public ImageAdapter(Context context, String[] mobileValues) {
        this.context = context;
        this.mobileValues = mobileValues;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(context);

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.mobile, null);

            // set value into textview
            TextView textView = (TextView) gridView
                    .findViewById(R.id.grid_item_label);
            textView.setText(mobileValues[position]);
            String customFont = "fonts/arlrdbd.ttf";
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), customFont);
            textView.setTypeface(typeface);
            // set image based on selected text
            ImageView imageView = (ImageView) gridView
                    .findViewById(R.id.grid_item_image);

            String mobile = mobileValues[position];

            if (mobile.equals("New Customer")) {
                imageView.setImageResource(R.drawable.add_cust_icon);
            } else if (mobile.equals("Edit Customer")) {
                imageView.setImageResource(R.drawable.edit_cust_icon);
            } else if (mobile.equals("Search Customer")) {
                imageView.setImageResource(R.drawable.search_cust_icon);
            } else if (mobile.equals("Customer Daily Entry")) {
                imageView.setImageResource(R.drawable.customer_daily_entry);
            } else if (mobile.equals("2 days Report")) {
                imageView.setImageResource(R.drawable.two_days_report);
            } else if (mobile.equals("50 Day Complete Report")) {
                imageView.setImageResource(R.drawable.fifty_days_report);
            } else if (mobile.equals("Delete Account Detail")) {
                imageView.setImageResource(R.drawable.delete_customer_detail);
            } else if (mobile.equals("Daily Entry Report")) {
                imageView.setImageResource(R.drawable.daily_entry_icon);
            } else if (mobile.equals("Delete Last Report")) {
                imageView.setImageResource(R.drawable.delete_last_record_report);
            }


        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }

    @Override
    public int getCount() {
        return mobileValues.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}