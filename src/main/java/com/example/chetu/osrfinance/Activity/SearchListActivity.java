package com.example.chetu.osrfinance.Activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chetu.osrfinance.Adapter.SearchListAdapter;
import com.example.chetu.osrfinance.Config;
import com.example.chetu.osrfinance.Helper.DatabaseHelper;
import com.example.chetu.osrfinance.Model.CustomerDetail;
import com.example.chetu.osrfinance.Model.User;
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

public class SearchListActivity extends Activity {
    public ArrayList<CustomerDetail> ln = new ArrayList<CustomerDetail>();
    public ArrayList<CustomerDetail> lnTemp = new ArrayList<CustomerDetail>();
    public ListView list_view;
    private EditText search_et;
    private SearchListAdapter searchListAdapter;
    private String searchmode;
    String ActivityName = "";
    ProgressDialog progress;
    public String rslt = "";
    private RelativeLayout mainRelative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search_list);

        Intent intent = getIntent();
        ActivityName = "" + intent.getStringExtra("ActivityName");

        searchmode = "normal";
        mainRelative = (RelativeLayout) findViewById(R.id.mainRelative);
        search_et = (EditText) findViewById(R.id.search_et);
        search_et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                // Call back the Adapter with current character to Filter
                //searchListAdapter.getFilter().filter(cs.toString());

                if (cs != null && cs.toString().length() > 0) {
                    searchmode = "search";
                    myfilter(cs);
                } else {
                    searchmode = "normal";
                    listShow();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });


        list_view = (ListView) findViewById(R.id.list_view);

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomerDetail jobs = null;
                if (searchmode.equalsIgnoreCase("normal")) {
                    jobs = ln.get(position);
                } else {
                    jobs = lnTemp.get(position);
                }

                Log.e("list_view", "position---->" + position);

                if (ActivityName.equalsIgnoreCase("SearchCustomer")) {
                    Intent intent = new Intent(SearchListActivity.this, CustomerFinanceDetailActivity.class);
                    intent.putExtra("CardNo", jobs.CardNo);
                    intent.putExtra("CustName", "" + jobs.CustName);
                    intent.putExtra("ContactNo", "" + jobs.ContactNo);
                    intent.putExtra("Amount", "" + jobs.Amount);
                    intent.putExtra("DailyAmt", "" + jobs.DailyAmt);
                    intent.putExtra("AmtDate", "" + jobs.AmtDate);
                    intent.putExtra("CustId", "" + jobs.CustId);

                    startActivity(intent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                } else {
                    Intent intent = new Intent(SearchListActivity.this, CustomerDetailActivity.class);
                    intent.putExtra("CustId", jobs.CustId);
                    intent.putExtra("CardNo", "" + jobs.CardNo);
                    intent.putExtra("CustName", "" + jobs.CustName);
                    intent.putExtra("ContactNo", "" + jobs.ContactNo);
                    intent.putExtra("Amount", "" + jobs.Amount);
                    intent.putExtra("DailyAmt", "" + jobs.DailyAmt);
                    intent.putExtra("AmtDate", jobs.AmtDate);
                    intent.putExtra("HomeAddress", jobs.HomeAddress);
                    intent.putExtra("BusiAddress", "" + jobs.BusiAddress);
                    intent.putExtra("GuarantorOrIntroducerName", "" + jobs.GuarantorOrIntroducerName);
                    intent.putExtra("GContactNo", "" + jobs.GContactNo);
                    intent.putExtra("PhotoPath", "" + jobs.PhotoPath);
                    intent.putExtra("UserId", "" + jobs.UserId);
                    intent.putExtra("ActivityName", "EditCustomer");
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }


            }

        });

        if (!isNetworkConnected()) {
            Snackbar snackbar = Snackbar
                    .make(mainRelative, "No internet connection!", Snackbar.LENGTH_LONG)
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

    private void listShow() {

        if (searchmode == "search") {
            searchListAdapter = new SearchListAdapter(SearchListActivity.this, lnTemp);

        } else {
            searchListAdapter = new SearchListAdapter(SearchListActivity.this, ln);
        }

        list_view.setAdapter(searchListAdapter);
        searchListAdapter.notifyDataSetChanged();

    }

    private void myfilter(CharSequence cs) {

        cs = cs.toString().toLowerCase();

        lnTemp = new ArrayList<CustomerDetail>();

        CustomerDetail vl;
        String vehicleno, vehiclecity, combinestring, run;

        if (cs != null && cs.toString().length() > 0) {

            for (int i = 0; ln.size() - 1 >= i; i++) {

                vl = ln.get(i);
                vehicleno = vl.CustName;
                //vehiclecity = vl.getLocation();
                //combinestring = vehicleno+vehiclecity;

                if (vehicleno.toLowerCase().contains(cs)) {
                    lnTemp.add(vl);
                }
            }
        }
        listShow();
    }

    private class LoadAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.e("inside onpre exe", "inside onpre exe");
            super.onPreExecute();
            progress = new ProgressDialog(SearchListActivity.this);
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
                        HttpPost httppost = new HttpPost(Config.YOUR_SERVER_URL + "getCustomerDetails.php");
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


                            //"CustId":"12","CardNo":"1","CustName":"roshan","ContactNo":"865223655","HomeAddress":"mumbai","BusiAddress":"mumbai","Amount":"1000","DailyAmt":"25","AmtDate":"15-6-2018","GuarantorOrIntroducerName":"xyz","GContactNo":"2445225522","PhotoPath":"http:\/\/petalsinfotech.com\/osrfinance\/images\/15289592361.JPEG","UserId":"1"
                            ln.add(new CustomerDetail(json_data.getInt("CustId"), json_data.getInt("CardNo"), json_data.getString("CustName"), json_data.getString("ContactNo"), json_data.getString("HomeAddress"), json_data.getString("BusiAddress"), json_data.getString("Amount"), json_data.getString("DailyAmt"), json_data.getString("AmtDate"), json_data.getString("GuarantorOrIntroducerName"), json_data.getString("GContactNo"), json_data.getString("PhotoPath"), json_data.getInt("UserId")));
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
                Log.e("Inside_IF", "LoginActivity--->");
                Toast.makeText(SearchListActivity.this, "Data Not Found", Toast.LENGTH_LONG).show();

            } else {
                listShow();
            }

        }

        protected void onProgressUpdate(Void... progress) {
            Log.e("inside progress exe", "inside progress exe");

        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) SearchListActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }
}
