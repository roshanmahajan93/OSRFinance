package com.example.chetu.osrfinance.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chetu.osrfinance.Model.CustomerFinance;
import com.example.chetu.osrfinance.Model.CustomerTwoAndFiftyReport;
import com.example.chetu.osrfinance.R;

import java.util.List;

/**
 * Created by Vinayak on 7/2/2018.
 */

public class CustomerTwoAndFiftyDayReportAdapter extends android.widget.BaseAdapter {

    Context context;
    List<CustomerTwoAndFiftyReport> JobsList;


    public CustomerTwoAndFiftyDayReportAdapter(Context context, List<CustomerTwoAndFiftyReport> modelList) {
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
            view = mInflater.inflate(R.layout.two_and_fifty_days_report_adapter_row, null);

            TextView CardNo_value_txt = (TextView) view.findViewById(R.id.CardNo_value_txt);
            TextView CustomerName_value_txt = (TextView) view.findViewById(R.id.CustomerName_value_txt);
            TextView ContactNo_value_txt = (TextView) view.findViewById(R.id.ContactNo_value_txt);
            TextView NoofDaysPending_value_txt = (TextView) view.findViewById(R.id.NoofDaysPending_value_txt);
            TextView Daily_value_txt = (TextView) view.findViewById(R.id.Daily_value_txt);
            TextView Amount_value_txt = (TextView) view.findViewById(R.id.Amount_value_txt);
            TextView GuarantorName_value_txt = (TextView) view.findViewById(R.id.GuarantorName_value_txt);
            TextView Guarantor_Contact_value_txt = (TextView) view.findViewById(R.id.Guarantor_Contact_value_txt);

            final CustomerTwoAndFiftyReport jobs = JobsList.get(i);
            CardNo_value_txt.setText("" + jobs.CardNo);
            CustomerName_value_txt.setText("" + jobs.CustName);
            ContactNo_value_txt.setText("" + jobs.ContactNo);
            NoofDaysPending_value_txt.setText("" + jobs.No_Of_Day_Pending);
            Daily_value_txt.setText("" + jobs.DailyAmt);
            Amount_value_txt.setText("" + jobs.Amount);
            GuarantorName_value_txt.setText("" + jobs.GuarantorOrIntroducerName);
            Guarantor_Contact_value_txt.setText("" + jobs.GContactNo);

        }
        return view;
    }


}
