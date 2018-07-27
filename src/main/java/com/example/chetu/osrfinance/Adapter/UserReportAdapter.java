package com.example.chetu.osrfinance.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chetu.osrfinance.Model.CustomerTwoAndFiftyReport;
import com.example.chetu.osrfinance.Model.UserDailyReport;
import com.example.chetu.osrfinance.R;

import java.util.List;

/**
 * Created by Vinayak on 7/10/2018.
 */

public class UserReportAdapter extends android.widget.BaseAdapter {

    Context context;
    List<UserDailyReport> JobsList;


    public UserReportAdapter(Context context, List<UserDailyReport> modelList) {
        this.context = context;
        this.JobsList = modelList;

    }


    @Override
    public int getCount() {
        return JobsList.size();

    }

    @Override
    public Object getItem(int i) {
        return JobsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = null;

        if (view == null) {

            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = mInflater.inflate(R.layout.user_report_activity_adapter_row, null);

            TextView CardNo_value_txt = (TextView) view.findViewById(R.id.CardNo_value_txt);
            TextView CustomerName_value_txt = (TextView) view.findViewById(R.id.CustomerName_value_txt);
            TextView Date_value_txt = (TextView) view.findViewById(R.id.Date_value_txt);
            TextView Total_Amount_value_txt = (TextView) view.findViewById(R.id.Total_Amount_value_txt);
            TextView User_name_value_txt = (TextView) view.findViewById(R.id.User_name_value_txt);


            final UserDailyReport jobs = JobsList.get(i);
            CardNo_value_txt.setText("" + jobs.card_no);
            CustomerName_value_txt.setText("" + jobs.CustName);
            Date_value_txt.setText("" + jobs.UDate);
            Total_Amount_value_txt.setText("" + jobs.total_amount);
            User_name_value_txt.setText("" + jobs.user_name);

        }
        return view;
    }


}
