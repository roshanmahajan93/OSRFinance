package com.example.chetu.osrfinance.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chetu.osrfinance.Adapter.CustomerFinanceListAdapter;
import com.example.chetu.osrfinance.Adapter.CustomerTwoAndFiftyDayReportAdapter;
import com.example.chetu.osrfinance.Config;
import com.example.chetu.osrfinance.Model.CustomerFinance;
import com.example.chetu.osrfinance.Model.CustomerTwoAndFiftyReport;
import com.example.chetu.osrfinance.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TwoDaysAndFiftyDaysReportActivity extends Activity {
    TextView txt_total_customer_Count_Value;
    public ArrayList<CustomerTwoAndFiftyReport> ln = new ArrayList<CustomerTwoAndFiftyReport>();
    public ListView list_view;
    private LinearLayout mainLinear;
    ProgressDialog progress;
    public String rslt = "";
    String ActivityName = "";

    CustomerTwoAndFiftyDayReportAdapter customerTwoAndFiftyDayReportAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_days_and_fifty_days_report);

        Intent intent = getIntent();
        ActivityName = "" + intent.getStringExtra("ActivityName");
        Log.e("ActivityName", "--->" + ActivityName);

        mainLinear = (LinearLayout) findViewById(R.id.LinearLayout3);

        txt_total_customer_Count_Value = (TextView) findViewById(R.id.txt_total_customer_Count_Value);

        list_view = (ListView) findViewById(R.id.list_view);

        PopUpDataBinding();

    }

    public void PopUpDataBinding() {
        ln.clear();
        if (!isNetworkConnected()) {
            Snackbar snackbar = Snackbar
                    .make(mainLinear, "No internet connection!", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    });
            // Changing message text color
            snackbar.setActionTextColor(Color.RED);
            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        } else {

            new LoadAsyncTask().execute("");

        }
    }


    private class LoadAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.e("inside onpre exe", "inside onpre exe");
            super.onPreExecute();
            progress = new ProgressDialog(TwoDaysAndFiftyDaysReportActivity.this);
            progress.setMessage("Loading. Please wait..");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setCanceledOnTouchOutside(false);
            progress.show();
        }

        @Override
        protected String doInBackground(String... params) {
            Log.e("inside doinback exe", "inside doinback exe");


            Thread thread = new Thread(new Runnable() {
                public void run() {
                    InputStream is = null;
                    String result = "";


                    try {
                        DefaultHttpClient httpclient = new DefaultHttpClient();
                        HttpPost httppost = null;
                        if (ActivityName.equalsIgnoreCase("2daysReport")) {
                            httppost = new HttpPost(Config.YOUR_SERVER_URL + "two_days_report.php");
                        } else {
                            httppost = new HttpPost(Config.YOUR_SERVER_URL + "fifty_days_report.php");
                        }


                        //Log.e("log_tag", "Enters TRY BLOCK 1");
                        HttpResponse response = httpclient.execute(httppost);
                        //Log.e("log_tag", "Enters TRY BLOCK 2");
                        HttpEntity entity = response.getEntity();
                        //Log.e("log_tag", "Enters TRY BLOCK 3");
                        is = entity.getContent();
                        //Log.e("log_tag", "Enters TRY BLOCK 4");

                    } catch (Exception e) {
                        Log.e("abc", "Enters FIRST CATCH BLOCK");
                        Log.e("xyz", "Error in http connection " + e.toString());
                    }

                    //convert response to string
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);

                        }
                        is.close();
                        // Log.e("def", sb.toString());
                        result = sb.toString();
                        if (sb.toString().equalsIgnoreCase("null") || sb.toString().equalsIgnoreCase("")) {
                            rslt = "Fail";
                        } else {
                            rslt = "";
                        }
                    } catch (Exception e) {
                        Log.e("log_tag", "Error converting result " + e.toString());
                    }

                    try {
                        //Log.e("log_tag", "Enters SECOND TRY BLOCK 1");
                        ln.clear();
                        JSONArray jArray = new JSONArray(result);
                        //Log.e("log_tag", "Enters SECOND TRY BLOCK 2");
                        for (int i = 0; i < jArray.length(); i++) {
                            //Log.e("log_tag", "Enters SECOND TRY BLOCK 3");
                            JSONObject json_data = jArray.getJSONObject(i);

                            ln.add(new CustomerTwoAndFiftyReport(json_data.getString("CardNo"), json_data.getString("CustName"), json_data.getString("ContactNo"), json_data.getString("No_Of_Day_Pending"), json_data.getString("DailyAmt"), json_data.getString("Amount"), json_data.getString("GuarantorOrIntroducerName"), json_data.getString("GContactNo")));
                        }
                    } catch (JSONException e) {

                        Log.e("log_tag", "Error parsing data " + e.toString());
                    }
                }
            });
            thread.start();

            try {
                thread.join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("inside post exe", "inside post exe");
            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }

            if (rslt.equalsIgnoreCase("Fail")) {
                Log.e("Inside_IF", "TwoDaysAndFiftyDays--->");
                Toast.makeText(TwoDaysAndFiftyDaysReportActivity.this, "Data Not Found", Toast.LENGTH_LONG).show();

            } else {
                listShow();
            }

        }

        protected void onProgressUpdate(Void... progress) {
            Log.e("inside progress exe", "inside progress exe");

        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) TwoDaysAndFiftyDaysReportActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }

    private void listShow() {
        txt_total_customer_Count_Value.setText("" + ln.size());
        customerTwoAndFiftyDayReportAdapter = new CustomerTwoAndFiftyDayReportAdapter(TwoDaysAndFiftyDaysReportActivity.this, ln);
        list_view.setAdapter(customerTwoAndFiftyDayReportAdapter);
        customerTwoAndFiftyDayReportAdapter.notifyDataSetChanged();
    }
}
