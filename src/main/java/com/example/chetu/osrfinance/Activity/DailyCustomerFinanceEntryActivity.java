package com.example.chetu.osrfinance.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DailyCustomerFinanceEntryActivity extends Activity implements View.OnClickListener {

    private String title;
    private String[] list;
    EditText et_date, et_penalty, et_remark, et_amount, et_cardNo, et_custName;
    Button btn_yes;
    private AlphaAnimation alphaAnimation;
    private int mYear, mMonth, mDay;

    CustomerFinanceDetailActivity customerFinanceDetailActivity;
    String Amount = "", AmtDate = "";
    int CustId = 0;
    ArrayList<CustomerFinance> ln = new ArrayList<CustomerFinance>();
    List<String> categories3 = new ArrayList<String>();

    int listItemClick;

    private TextView textView3;

    private String searchmode;
    ProgressDialog barProgressDialog;
    private LinearLayout dailyvehanaLinearLayoutForCardNo, dailyvehanaLinearLayoutForCustName;
    private ListView lstDailyVehParaCardNo, lstDailyVehParaVehNameCustName;
    ProgressDialog progress;
    public String rslt = "";
    private LinearLayout linearLayoutmain;
    private SharedPreferences prefs;
    private String prefName = "UserDetails";
    String UserId = "";
    String UserName = "";
    String SelectedDate = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_customer_finance_entry);

        String customFont = "fonts/arlrdbd.ttf";
        Typeface typeface = Typeface.createFromAsset(getAssets(), customFont);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView3.setTypeface(typeface);

        // DatabaseHelper.init(DailyCustomerFinanceEntryActivity.this);
        alphaAnimation = new AlphaAnimation(3.0F, 0.4F);

        prefs = getSharedPreferences(prefName, Context.MODE_PRIVATE);

        UserId = prefs.getString("UId", "");
        UserName = prefs.getString("UserName", "");
        Log.e("DailyEntryActivity", "DailyEntryActivity---->" + UserId);

        searchmode = "normal";

        //barProgressDialog = ProgressDialog.show(DailyCustomerFinanceEntryActivity.this, "Please wait ...", "Downloading...", true);
        //barProgressDialog.setCancelable(false);

        linearLayoutmain = (LinearLayout) findViewById(R.id.LinearLayoutmain);

        dailyvehanaLinearLayoutForCardNo = (LinearLayout) findViewById(R.id.dailyvehanaLinearLayoutForCardNo);
        dailyvehanaLinearLayoutForCustName = (LinearLayout) findViewById(R.id.dailyvehanaLinearLayoutForCustName);

        lstDailyVehParaCardNo = (ListView) findViewById(R.id.lstDailyVehParaCardNo);
        lstDailyVehParaVehNameCustName = (ListView) findViewById(R.id.lstDailyVehParaVehNameCustName);

        et_amount = (EditText) findViewById(R.id.et_amount);
        et_cardNo = (EditText) findViewById(R.id.et_cardNo);
        et_date = (EditText) findViewById(R.id.et_date);
        et_penalty = (EditText) findViewById(R.id.et_penalty);
        et_custName = (EditText) findViewById(R.id.et_custName);


        et_date.setInputType(InputType.TYPE_NULL);
        et_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideKeyboard(v);
            }
        });

        et_date.setOnClickListener(this);

        btn_yes = (Button) findViewById(R.id.btn_yes);
        // btn_no=(Button)findViewById(R.id.btn_no);

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
    }

    public boolean validate() {
        boolean valid = true;

        SelectedDate = et_date.getText().toString();


        if (SelectedDate.isEmpty()) {
            et_date.setError("Date should not left blank");
            valid = false;
        } else {
            et_date.setError(null);
        }


        return valid;
    }

    private class LoadAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.e("inside onpre exe", "inside onpre exe");
            super.onPreExecute();
            progress = new ProgressDialog(DailyCustomerFinanceEntryActivity.this);
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
                    nameValuePairs.add(new BasicNameValuePair("FDate", et_date.getText().toString() + ""));
                    nameValuePairs.add(new BasicNameValuePair("FAmount", et_amount.getText().toString() + ""));
                    nameValuePairs.add(new BasicNameValuePair("FineAndPenaltys", et_penalty.getText().toString() + ""));
                    nameValuePairs.add(new BasicNameValuePair("UserId", UserId));
                    nameValuePairs.add(new BasicNameValuePair("UserName", UserName));

                    try {
                        DefaultHttpClient httpclient = new DefaultHttpClient();
                        HttpPost httppost = new HttpPost(Config.YOUR_SERVER_URL + "update_customer_finance_detail.php");
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
                Toast.makeText(DailyCustomerFinanceEntryActivity.this, "Error While Inserting/Updating", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(DailyCustomerFinanceEntryActivity.this, "" + rslt, Toast.LENGTH_LONG).show();
            }

        }

        protected void onProgressUpdate(Void... progress) {
            Log.e("inside progress exe", "inside progress exe");

        }
    }


    @Override
    public void onClick(View v) {
        if (v == et_date) {
            v.startAnimation(alphaAnimation);
            et_date.setError(null);

            // Process to get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            // Launch Date Picker Dialog
            final DatePickerDialog dpd = new DatePickerDialog(DailyCustomerFinanceEntryActivity.this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            monthOfYear++;
                            String d, m;

                            if (monthOfYear < 10) {
                                m = "0" + monthOfYear;
                            } else {
                                m = "" + monthOfYear;
                            }

                            if (dayOfMonth < 10) {
                                d = "0" + dayOfMonth;
                            } else {
                                d = "" + dayOfMonth;
                            }


                            et_date.setText(d + "-" + m + "-" + year);


                        }
                    }, mYear, mMonth, mDay);
            dpd.show();

            /*try {
                SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat format2 = new SimpleDateFormat("dd-MMM-yyyy");
                Date date = format1.parse(""+AmtDate);
                //Log.e("getDate()",""+format2.format(date));
                Date dtt = format2.parse(""+format2.format(date));

                //Date dt = new Date();
                Calendar c1 = Calendar.getInstance();
                c1.setTime(dtt);
                c1.add(Calendar.DATE, 1);
                dtt = c1.getTime();

                long milliseconds = dtt.getTime();
                dpd.getDatePicker().setMinDate(milliseconds);
                //System.out.println(format2.format(date));

            } catch (ParseException e) {
                e.printStackTrace();
            }*/
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) DailyCustomerFinanceEntryActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) DailyCustomerFinanceEntryActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}