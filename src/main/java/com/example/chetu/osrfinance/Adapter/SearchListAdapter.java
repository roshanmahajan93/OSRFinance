package com.example.chetu.osrfinance.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.chetu.osrfinance.Model.CustomerDetail;
import com.example.chetu.osrfinance.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chetu on 6/3/2018.
 */
public class SearchListAdapter extends BaseAdapter implements Filterable {

    Context context;
    List<CustomerDetail> JobsList;
    List<CustomerDetail> mOriginalValues;

    public SearchListAdapter(Context context, List<CustomerDetail> modelList) {
        this.context = context;
        this.JobsList = modelList;
        this.mOriginalValues = modelList;
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
            view = mInflater.inflate(R.layout.searchlist_adapter_row, null);

            TextView serialNo_value_txt = (TextView) view.findViewById(R.id.serialNo_value_txt);
            TextView jobName_value_txt = (TextView) view.findViewById(R.id.jobName_value_txt);
            TextView expiryDate_value_txt = (TextView) view.findViewById(R.id.expiryDate_value_txt);


            final CustomerDetail jobs = JobsList.get(i);
            serialNo_value_txt.setText("" + jobs.CardNo);
            jobName_value_txt.setText("" + jobs.CustName);
            expiryDate_value_txt.setText("" + jobs.Amount);

            // click listiner for remove button

        }
        return view;
    }


    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                JobsList = (ArrayList<CustomerDetail>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                List<CustomerDetail> FilteredArrList = new ArrayList<CustomerDetail>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<CustomerDetail>(JobsList); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        String data = mOriginalValues.get(i).CustName;
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            // FilteredArrList.add(new Jobs(mOriginalValues.get(i).id,mOriginalValues.get(i).SerialNo,mOriginalValues.get(i).JobName,mOriginalValues.get(i).ExpiryDate,mOriginalValues.get(i).Stauts));

                            FilteredArrList.add(new CustomerDetail(mOriginalValues.get(i).CustId, mOriginalValues.get(i).CardNo, mOriginalValues.get(i).CustName, mOriginalValues.get(i).ContactNo, mOriginalValues.get(i).HomeAddress, mOriginalValues.get(i).BusiAddress, mOriginalValues.get(i).Amount, mOriginalValues.get(i).DailyAmt, mOriginalValues.get(i).AmtDate, mOriginalValues.get(i).GuarantorOrIntroducerName, mOriginalValues.get(i).GContactNo, mOriginalValues.get(i).PhotoPath, mOriginalValues.get(i).UserId));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
}
