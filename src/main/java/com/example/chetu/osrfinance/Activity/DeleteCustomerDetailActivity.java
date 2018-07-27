package com.example.chetu.osrfinance.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chetu.osrfinance.Config;
import com.example.chetu.osrfinance.Helper.DatabaseHelper;
import com.example.chetu.osrfinance.Model.CustomerFinance;
import com.example.chetu.osrfinance.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DeleteCustomerDetailActivity extends Activity {

    EditText et_cardNo;
    Button btn_yes, btn_delete_last;

    int CustId = 0;

    int listItemClick;
    private AlphaAnimation alphaAnimation;
    private TextView textView3;

    private String searchmode;

    ProgressDialog progress;
    public String rslt = "";
    private LinearLayout linearLayoutmain;
    String cardNo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_customer_detail);

        String customFont = "fonts/arlrdbd.ttf";
        Typeface typeface = Typeface.createFromAsset(getAssets(), customFont);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView3.setTypeface(typeface);

        DatabaseHelper.init(DeleteCustomerDetailActivity.this);
        alphaAnimation = new AlphaAnimation(3.0F, 0.4F);

        searchmode = "normal";

        linearLayoutmain = (LinearLayout) findViewById(R.id.LinearLayoutmain);

        et_cardNo = (EditText) findViewById(R.id.et_cardNo);

        btn_yes = (Button) findViewById(R.id.btn_yes);
        btn_delete_last = (Button) findViewById(R.id.btn_delete_last);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ln.add(new CustomerFinance(""+et_date.getText().toString(),""+et_amount.getText().toString(),""+et_penalty.getText().toString(),""+et_remark.getText().toString(),CustId));
                //String temp = DatabaseHelper.insertCustomerFinanceDetail(ln);
                //Toast.makeText(DailyCustomerFinanceEntryActivity.this, "" + temp, Toast.LENGTH_LONG).show();

                if (!isNetworkConnected()) {
                    Snackbar snackbar = Snackbar
                            .make(linearLayoutmain, "No internet connection!", Snackbar.LENGTH_LONG)
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
                    if (validate()) {
                        new LoadAsyncTask().execute("");
                    }

                }

            }
        });

        btn_delete_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ln.add(new CustomerFinance(""+et_date.getText().toString(),""+et_amount.getText().toString(),""+et_penalty.getText().toString(),""+et_remark.getText().toString(),CustId));
                //String temp = DatabaseHelper.insertCustomerFinanceDetail(ln);
                //Toast.makeText(DailyCustomerFinanceEntryActivity.this, "" + temp, Toast.LENGTH_LONG).show();

                if (!isNetworkConnected()) {
                    Snackbar snackbar = Snackbar
                            .make(linearLayoutmain, "No internet connection!", Snackbar.LENGTH_LONG)
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
                    if (validate()) {
                        new LoadAsyncTask2().execute("");
                    }

                }

            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        cardNo = et_cardNo.getText().toString();


        if (cardNo.isEmpty()) {
            et_cardNo.setError("Card No should not left blank");
            valid = false;
        } else {
            et_cardNo.setError(null);
        }


        return valid;
    }

    private class LoadAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.e("inside onpre exe", "inside onpre exe");
            super.onPreExecute();
            progress = new ProgressDialog(DeleteCustomerDetailActivity.this);
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

                    nameValuePairs.add(new BasicNameValuePair("CardNo", et_cardNo.getText().toString() + ""));

                    try {
                        DefaultHttpClient httpclient = new DefaultHttpClient();
                        HttpPost httppost = new HttpPost(Config.YOUR_SERVER_URL + "delete_old_customer_data.php");
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
                        Log.e("def", sb.toString());
                        if (sb.toString().equalsIgnoreCase("null") || sb.toString().equalsIgnoreCase("")) {
                            rslt = "Fail";
                        } else {
                            rslt = "" + sb.toString();
                        }
                    } catch (Exception e) {
                        Log.e("log_tag", "Error converting result " + e.toString());
                    }

                    /*try {
                        //Log.e("log_tag", "Enters SECOND TRY BLOCK 1");
                        JSONArray jArray = new JSONArray(result);
                        //Log.e("log_tag", "Enters SECOND TRY BLOCK 2");
                        for (int i = 0; i < jArray.length(); i++) {
                            //Log.e("log_tag", "Enters SECOND TRY BLOCK 3");
                            JSONObject json_data = jArray.getJSONObject(i);
                            //Log.e("log_tag", "Enters SECOND TRY BLOCK 4");

                            //uid = json_data.getInt("id");
                            Log.e("id", "id" + json_data.getInt("UId"));
                            Log.e("LoginId", "LoginId" + json_data.getString("UserName"));
                            Log.e("name", "name" + json_data.getString("UserPassword"));
                            Log.e("Usr_type", "Usr_type" + json_data.getString("CompanyName"));

                            list.add(new User(json_data.getInt("UId"), json_data.getString("UserName"), json_data.getString("UserPassword"), json_data.getString("CompanyName")));
                        }
                    } catch (JSONException e) {

                        Log.e("log_tag", "Error parsing data " + e.toString());
                    }*/
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
                Toast.makeText(DeleteCustomerDetailActivity.this, "Error While Deleting", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(DeleteCustomerDetailActivity.this, "" + rslt, Toast.LENGTH_LONG).show();
            }

        }

        protected void onProgressUpdate(Void... progress) {
            Log.e("inside progress exe", "inside progress exe");

        }
    }

    private class LoadAsyncTask2 extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.e("inside onpre exe", "inside onpre exe");
            super.onPreExecute();
            progress = new ProgressDialog(DeleteCustomerDetailActivity.this);
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

                    nameValuePairs.add(new BasicNameValuePair("CardNo", et_cardNo.getText().toString() + ""));

                    try {
                        DefaultHttpClient httpclient = new DefaultHttpClient();
                        HttpPost httppost = new HttpPost(Config.YOUR_SERVER_URL + "delete_last_customer_finance_detail_record.php");
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
                        Log.e("def", sb.toString());
                        if (sb.toString().equalsIgnoreCase("null") || sb.toString().equalsIgnoreCase("")) {
                            rslt = "Fail";
                        } else {
                            rslt = "" + sb.toString();
                        }
                    } catch (Exception e) {
                        Log.e("log_tag", "Error converting result " + e.toString());
                    }

                    /*try {
                        //Log.e("log_tag", "Enters SECOND TRY BLOCK 1");
                        JSONArray jArray = new JSONArray(result);
                        //Log.e("log_tag", "Enters SECOND TRY BLOCK 2");
                        for (int i = 0; i < jArray.length(); i++) {
                            //Log.e("log_tag", "Enters SECOND TRY BLOCK 3");
                            JSONObject json_data = jArray.getJSONObject(i);
                            //Log.e("log_tag", "Enters SECOND TRY BLOCK 4");

                            //uid = json_data.getInt("id");
                            Log.e("id", "id" + json_data.getInt("UId"));
                            Log.e("LoginId", "LoginId" + json_data.getString("UserName"));
                            Log.e("name", "name" + json_data.getString("UserPassword"));
                            Log.e("Usr_type", "Usr_type" + json_data.getString("CompanyName"));

                            list.add(new User(json_data.getInt("UId"), json_data.getString("UserName"), json_data.getString("UserPassword"), json_data.getString("CompanyName")));
                        }
                    } catch (JSONException e) {

                        Log.e("log_tag", "Error parsing data " + e.toString());
                    }*/
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
                Toast.makeText(DeleteCustomerDetailActivity.this, "Error While Deleting", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(DeleteCustomerDetailActivity.this, "" + rslt, Toast.LENGTH_LONG).show();
            }

        }

        protected void onProgressUpdate(Void... progress) {
            Log.e("inside progress exe", "inside progress exe");

        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) DeleteCustomerDetailActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }


}