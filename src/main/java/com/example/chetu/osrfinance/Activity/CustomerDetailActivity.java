package com.example.chetu.osrfinance.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.chetu.osrfinance.AppController;
import com.example.chetu.osrfinance.Config;
import com.example.chetu.osrfinance.Helper.DatabaseHelper;
import com.example.chetu.osrfinance.Model.CustomerDetail;
import com.example.chetu.osrfinance.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CustomerDetailActivity extends Activity implements OnClickListener {
    public static final String DATEPICKER_TAG = "datepicker";
    public final int REQUEST_EXTERNAL_STOREAGE_RESULT = 1;
    public int BtnClickView = 0;
    public String rslt = "";
    int CardNo = 0;
    DatePickerDialog datePickerDialog;
    Calendar c;

    Date datecurrent = new Date();
    Date dateButton = new Date();
    String data = "", decodeString = "";
    ArrayList<CustomerDetail> CustomerList = new ArrayList<CustomerDetail>();
    ProgressDialog progress;
    String ActivityName = "", PhotoPath = "";

    public int CustId = 1;
    public String CardNos = "";
    public String CustName = "";
    public String ContactNo = "";
    public String HomeAddress = "";
    public String BusiAddress = "";
    public String Amount = "";
    public String DailyAmt = "";
    public String AmtDate = "";
    public String GuarantorOrIntroducerName = "";
    public String GContactNo = "";
    public int UserId = 1;

    private ImageLoader mImageLoader;
    private TextView textView3;
    private TextView txt_card_no;
    private NetworkImageView img_cust_img;
    private ImageView img_cust_img2;
    private Button btn_camera, btn_submit;
    private Bitmap bitmap;
    private Uri filePath;
    private int mYear, mMonth, mDay;
    private AlphaAnimation alphaAnimation;
    private SharedPreferences prefs;
    private String prefName = "UserDetails";
    private RelativeLayout relativeLayoutmain;
    @InjectView(R.id.et_cust_name)
    EditText et_cust_name;
    @InjectView(R.id.et_contact_no)
    EditText et_contact_no;
    @InjectView(R.id.et_home_address)
    EditText et_home_address;
    @InjectView(R.id.et_business_addrress)
    EditText et_business_addrress;
    @InjectView(R.id.et_amount)
    EditText et_amount;
    @InjectView(R.id.et_daily_amount)
    EditText et_daily_amount;
    @InjectView(R.id.et_date)
    EditText et_date;
    @InjectView(R.id.et_granter_name)
    EditText et_granter_name;
    @InjectView(R.id.et_granter_contact)
    EditText et_granter_contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_customer_detail);
        ButterKnife.inject(this);
        //DatabaseHelper.init(CustomerDetailActivity.this);

        String customFont = "fonts/arlrdbd.ttf";
        Typeface typeface = Typeface.createFromAsset(getAssets(), customFont);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView3.setTypeface(typeface);

        alphaAnimation = new AlphaAnimation(3.0F, 0.4F);

        final Calendar calendar = Calendar.getInstance();
        c = Calendar.getInstance();

        txt_card_no = (TextView) findViewById(R.id.txt_card_no);
        et_daily_amount = (EditText) findViewById(R.id.et_daily_amount);
        et_cust_name = (EditText) findViewById(R.id.et_cust_name);
        et_contact_no = (EditText) findViewById(R.id.et_contact_no);
        et_home_address = (EditText) findViewById(R.id.et_home_address);
        et_business_addrress = (EditText) findViewById(R.id.et_business_addrress);
        et_amount = (EditText) findViewById(R.id.et_amount);
        et_date = (EditText) findViewById(R.id.et_date);
        et_granter_name = (EditText) findViewById(R.id.et_granter_name);
        et_granter_contact = (EditText) findViewById(R.id.et_granter_contact);
        img_cust_img = (NetworkImageView) findViewById(R.id.img_cust_img);
        img_cust_img.setVisibility(View.GONE);
        img_cust_img2 = (ImageView) findViewById(R.id.img_cust_img2);
        btn_camera = (Button) findViewById(R.id.btn_camera);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        relativeLayoutmain = (RelativeLayout) findViewById(R.id.relativeLayoutmain);

        mImageLoader = AppController.getInstance().getImageLoader();


        Intent intent = getIntent();
        ActivityName = "" + intent.getStringExtra("ActivityName");
        PhotoPath = "" + intent.getStringExtra("PhotoPath");
        CustId = intent.getIntExtra("CustId", 0);
        CardNos = intent.getStringExtra("CardNo");
        CustName = "" + intent.getStringExtra("CustName");
        ContactNo = "" + intent.getStringExtra("ContactNo");
        HomeAddress = "" + intent.getStringExtra("HomeAddress");
        BusiAddress = "" + intent.getStringExtra("BusiAddress");
        Amount = "" + intent.getStringExtra("Amount");
        DailyAmt = "" + intent.getStringExtra("DailyAmt");
        AmtDate = "" + intent.getStringExtra("AmtDate");
        GuarantorOrIntroducerName = "" + intent.getStringExtra("GuarantorOrIntroducerName");
        GContactNo = "" + intent.getStringExtra("GContactNo");
        UserId = intent.getIntExtra("UserId", 0);


        if (ActivityName.equalsIgnoreCase("EditCustomer")) {
            txt_card_no.setText("" + CardNos);
            et_daily_amount.setText(DailyAmt);
            et_cust_name.setText(CustName);
            et_contact_no.setText(ContactNo);
            et_home_address.setText(HomeAddress);
            et_business_addrress.setText(BusiAddress);
            et_amount.setText(Amount);
            et_date.setText(AmtDate);
            et_granter_name.setText(GuarantorOrIntroducerName);
            et_granter_contact.setText(GContactNo);
            if (!PhotoPath.equalsIgnoreCase("")) {
                img_cust_img.setVisibility(View.VISIBLE);
                img_cust_img2.setVisibility(View.GONE);
                img_cust_img.setImageUrl("" + PhotoPath, mImageLoader);
            }


        } else {
            if (!isNetworkConnected()) {
                Snackbar snackbar = Snackbar
                        .make(relativeLayoutmain, "No internet connection!", Snackbar.LENGTH_LONG)
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

                new LoadAsyncTask2().execute("");


            }
        }


        et_date.setInputType(InputType.TYPE_NULL);
        et_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideKeyboard(v);
            }
        });

        et_date.setOnClickListener(this);

        et_amount.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                // Call back the Adapter with current character to Filter
                String tempcs = "" + cs;
                if (!tempcs.equalsIgnoreCase("")) {

                    int number = Integer.parseInt(tempcs);
                    if (number <= 1000) {
                        et_daily_amount.setText("25");
                    } else {
                        et_daily_amount.setText("" + ((number / 1000) * 25));
                    }
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

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                BtnClickView = 1;

                if (ContextCompat.checkSelfPermission(CustomerDetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectImage();
                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        Toast.makeText(CustomerDetailActivity.this, "External Storage Permission Required To Store Image", Toast.LENGTH_LONG).show();
                    }
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STOREAGE_RESULT);
                }
            }
        });

        btn_submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                /*CustomerList.clear();
                CustomerList.add(new CustomerDetail(0, Integer.parseInt(txt_card_no.getText().toString()), "" + et_cust_name.getText().toString(), "" + et_contact_no.getText().toString(), "" + et_home_address.getText().toString(), "" + et_business_addrress.getText().toString(), "" + et_amount.getText().toString(), "" + txt_daily_amount.getText().toString(), "" + et_date.getText().toString(), "" + et_granter_name.getText().toString(), "" + et_granter_contact.getText().toString(), "", 1));
                String temp = DatabaseHelper.insertCustomerDetail(CustomerList);
                Toast.makeText(CustomerDetailActivity.this, "" + temp, Toast.LENGTH_LONG).show();*/

                if (!isNetworkConnected()) {
                    Snackbar snackbar = Snackbar
                            .make(relativeLayoutmain, "No internet connection!", Snackbar.LENGTH_LONG)
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

        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STOREAGE_RESULT) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            } else {
                Toast.makeText(CustomerDetailActivity.this, "External storage permission has not been granted, cannot be saved images ", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }
    }

    //....to choose images....
    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(CustomerDetailActivity.this);

        //builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);

                } /*else if (options[item].equals("Choose from Gallery")) {
                    //Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
                    //startActivityForResult(i, 200);
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                }*/ else if (options[item].equals("Cancel")) {

                    //profile_pic.setImageResource(R.drawable.ic_user_bg_white);
                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

//        Log.e("data","---->"+data.getExtras().get("data"));

        if (resultCode == CustomerDetailActivity.this.RESULT_OK) {

            if (requestCode == 1) {
                bitmap = (Bitmap) data.getExtras().get("data");
                if (BtnClickView == 1) {

                    img_cust_img.setVisibility(View.GONE);
                    img_cust_img2.setVisibility(View.VISIBLE);

                    img_cust_img2.setImageBitmap(bitmap);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream);
                    byte[] image = stream.toByteArray();

                    decodeString = Base64.encodeToString(image, Base64.DEFAULT);

                }
            }
        }
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) CustomerDetailActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
            final DatePickerDialog dpd = new DatePickerDialog(this,
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

                            String currdate = et_date.getText().toString().trim();

                            SimpleDateFormat dfDate = new SimpleDateFormat("dd-MM-yyyy");

                            data = dfDate.format(c.getTime());

                            try {
                                datecurrent = dfDate.parse(data.toString().trim());
                                dateButton = dfDate.parse(currdate.toString().trim());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, mYear, mMonth, mDay);
            dpd.show();
        }

    }

    private class LoadAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.e("inside onpre exe", "inside onpre exe");
            super.onPreExecute();
            progress = new ProgressDialog(CustomerDetailActivity.this);
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
                    nameValuePairs.add(new BasicNameValuePair("CardNo", txt_card_no.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("CustName", et_cust_name.getText().toString() + ""));
                    nameValuePairs.add(new BasicNameValuePair("ContactNo", et_contact_no.getText().toString() + ""));
                    nameValuePairs.add(new BasicNameValuePair("HomeAddress", et_home_address.getText().toString() + ""));
                    nameValuePairs.add(new BasicNameValuePair("BusiAddress", et_business_addrress.getText().toString() + ""));
                    nameValuePairs.add(new BasicNameValuePair("Amount", et_amount.getText().toString() + ""));
                    nameValuePairs.add(new BasicNameValuePair("DailyAmt", et_daily_amount.getText().toString() + ""));
                    nameValuePairs.add(new BasicNameValuePair("AmtDate", et_date.getText().toString() + ""));
                    nameValuePairs.add(new BasicNameValuePair("GuarantorOrIntroducerName", et_granter_name.getText().toString() + ""));
                    nameValuePairs.add(new BasicNameValuePair("GContactNo", et_granter_contact.getText().toString() + ""));
                    nameValuePairs.add(new BasicNameValuePair("PhotoPath", PhotoPath));
                    nameValuePairs.add(new BasicNameValuePair("UserId", UserId + ""));
                    nameValuePairs.add(new BasicNameValuePair("DecodeString", "" + decodeString));

                    try {
                        DefaultHttpClient httpclient = new DefaultHttpClient();
                        HttpPost httppost = new HttpPost(Config.YOUR_SERVER_URL + "insert_and_update_customer_detail.php");
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
                Toast.makeText(CustomerDetailActivity.this, "Error While Inserting/Updating", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(CustomerDetailActivity.this, "" + rslt, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(CustomerDetailActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.enter, R.anim.exit);


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
            progress = new ProgressDialog(CustomerDetailActivity.this);
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
                        HttpPost httppost = new HttpPost(Config.YOUR_SERVER_URL + "getCardNo.php");
                        //httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
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
                            rslt = "";
                        } else {
                            rslt = "" + sb.toString();
                        }
                    } catch (Exception e) {
                        Log.e("log_tag", "Error converting result " + e.toString());
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

            if (rslt.equalsIgnoreCase("")) {
                CardNo = 1;
                txt_card_no.setText("" + CardNo);

            } else {
                CardNo = 1 + (Integer.parseInt("" + rslt));
                txt_card_no.setText("" + CardNo);

            }

        }

        protected void onProgressUpdate(Void... progress) {
            Log.e("inside progress exe", "inside progress exe");

        }
    }
}
