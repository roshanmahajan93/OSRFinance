package com.example.chetu.osrfinance.Activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chetu.osrfinance.Adapter.CustomerFinanceListAdapter;
import com.example.chetu.osrfinance.Adapter.SearchListAdapter;
import com.example.chetu.osrfinance.Config;
import com.example.chetu.osrfinance.Fragment.Popup_Fragment;
import com.example.chetu.osrfinance.Helper.DatabaseHelper;
import com.example.chetu.osrfinance.Model.CustomerDetail;
import com.example.chetu.osrfinance.Model.CustomerFinance;
import com.example.chetu.osrfinance.R;
import com.example.chetu.osrfinance.Service.CustomerReportPDF;

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
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CustomerFinanceDetailActivity extends Activity {

    String CustId = "", CardNo = "", CustName = "", ContactNo = "", Amount = "", DailyAmt = "", AmtDate = "";
    TextView txt_card_no, txt_CustName, txt_ContactNo, txt_Amount, txt_DailyAmt, txt_AmtDate;
    public ArrayList<CustomerFinance> ln = new ArrayList<CustomerFinance>();
    public ListView list_view;
    private LinearLayout mainLinear;
    ProgressDialog progress;
    public String rslt = "";
    Button btn_share;

    private CustomerFinanceListAdapter customerFinanceListAdapter;
    public CustomerReportPDF customerReportPDF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_customer_finance_detail);

        //DatabaseHelper.init(CustomerFinanceDetailActivity.this);
        customerReportPDF = new CustomerReportPDF();

        Intent intent = getIntent();
        CardNo = "" + intent.getIntExtra("CardNo", 0);
        CustName = "" + intent.getStringExtra("CustName");
        ContactNo = "" + intent.getStringExtra("ContactNo");
        Amount = "" + intent.getStringExtra("Amount");
        DailyAmt = "" + intent.getStringExtra("DailyAmt");
        AmtDate = "" + intent.getStringExtra("AmtDate");
        CustId = intent.getStringExtra("CustId");
        Log.e("CustId", "CustId--->" + CustId);

        mainLinear = (LinearLayout) findViewById(R.id.mainLinear);

        btn_share = (Button) findViewById(R.id.btn_share);
        txt_card_no = (TextView) findViewById(R.id.txt_card_no);
        txt_CustName = (TextView) findViewById(R.id.txt_CustName);
        txt_ContactNo = (TextView) findViewById(R.id.txt_ContactNo);
        txt_Amount = (TextView) findViewById(R.id.txt_Amount);
        txt_DailyAmt = (TextView) findViewById(R.id.txt_DailyAmt);
        txt_AmtDate = (TextView) findViewById(R.id.txt_AmtDate);

        txt_card_no.setText(CardNo);
        txt_CustName.setText(CustName);
        txt_ContactNo.setText(ContactNo);
        txt_Amount.setText(Amount);
        txt_DailyAmt.setText(DailyAmt);
        txt_AmtDate.setText(AmtDate);


        list_view = (ListView) findViewById(R.id.list_view);

        PopUpDataBinding();

        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customerReportPDF.write(CardNo, ln, CardNo, CustName, ContactNo, Amount, DailyAmt, AmtDate)) {
                    File outputFile = new File(Environment.getExternalStorageDirectory() + "/OSRFinanceFile/", "PPR_" + CardNo + ".pdf");
                    Uri uri = Uri.fromFile(outputFile);

                    Intent share = new Intent();
                    share.setAction(Intent.ACTION_SEND);
                    share.setType("application/pdf");
                    share.putExtra(Intent.EXTRA_STREAM, uri);
                    share.setPackage("com.whatsapp");
                    startActivity(share);
                } else {
                    Toast.makeText(CustomerFinanceDetailActivity.this, "File Not Created", Toast.LENGTH_LONG).show();
                }


            }
        });

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
            progress = new ProgressDialog(CustomerFinanceDetailActivity.this);
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

                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("CustId", CustId + ""));
                    try {
                        DefaultHttpClient httpclient = new DefaultHttpClient();
                        HttpPost httppost = new HttpPost(Config.YOUR_SERVER_URL + "get_customerfinancedetail.php");
                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
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


                            ln.add(new CustomerFinance(json_data.getString("FDate"), json_data.getString("FAmount"), json_data.getString("FineAndPenalty"), json_data.getString("FRemark")));
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
                Toast.makeText(CustomerFinanceDetailActivity.this, "Data Not Found", Toast.LENGTH_LONG).show();

            } else {
                listShow();
            }

        }

        protected void onProgressUpdate(Void... progress) {
            Log.e("inside progress exe", "inside progress exe");

        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) CustomerFinanceDetailActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }

    private void listShow() {
        customerFinanceListAdapter = new CustomerFinanceListAdapter(CustomerFinanceDetailActivity.this, ln);
        list_view.setAdapter(customerFinanceListAdapter);
        customerFinanceListAdapter.notifyDataSetChanged();
    }


}
