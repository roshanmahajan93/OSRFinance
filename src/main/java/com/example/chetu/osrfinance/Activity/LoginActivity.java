package com.example.chetu.osrfinance.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chetu.osrfinance.Config;
import com.example.chetu.osrfinance.Helper.DatabaseHelper;
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
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends Activity {
    public static List<User> dataList = new ArrayList<User>();
    public String rslt = "";
    public int UId = 0;
    public String UserName = "";
    public ArrayList<User> list = new ArrayList<User>();
    private TextView txt_login;
    @InjectView(R.id.et_loginid)
    EditText _nameText;
    // @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.et_password)
    EditText _passwordText;
    String logout, Remember;
    String LoginId = "";
    String password = "";
    ProgressDialog progress;
    CheckBox checkBox;
    private EditText et_loginid, et_password;
    private Button btn_submit;
    private LinearLayout linearMain;
    private SharedPreferences prefs;
    private String prefName = "UserDetails";
    private long firstTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        DatabaseHelper.init(LoginActivity.this);
        ButterKnife.inject(this);

        String customFont = "fonts/HARLOWSI.TTF";
        Typeface typeface = Typeface.createFromAsset(getAssets(), customFont);

        linearMain = (LinearLayout) findViewById(R.id.linearMain);
        et_loginid = (EditText) findViewById(R.id.et_loginid);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        txt_login = (TextView) findViewById(R.id.txt_login);
        txt_login.setTypeface(typeface);

        prefs = getSharedPreferences(prefName, Context.MODE_PRIVATE);

        logout = prefs.getString("Logout", "");
        Remember = prefs.getString("Remember", "0");
        Log.e("LoginActivity", "LoginActivity---->" + logout);
        Log.e("logout", "logout" + logout);

        if (logout.equalsIgnoreCase("1") || Remember.equalsIgnoreCase("0")) {
            prefs = LoginActivity.this.getSharedPreferences(prefName, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("UId", "");
            editor.putString("UserName", "");
            editor.commit();
        }

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isNetworkConnected()) {
                    Snackbar snackbar = Snackbar
                            .make(linearMain, "No internet connection!", Snackbar.LENGTH_LONG)
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
                        // toastShow();


                        /*Intent i =new Intent(LoginActivity.this,JobListActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.enter, R.anim.exit);*/
                        new LoadAsyncTask().execute("");

                        //UserLoginDetail(LoginId,password);

                    }
                }
            }
        });

    }

    public boolean validate() {
        boolean valid = true;

        LoginId = et_loginid.getText().toString();
        password = et_password.getText().toString();

        if (LoginId.isEmpty()) {
            et_loginid.setError("Login ID should not left blank");
            valid = false;
        } else {
            et_loginid.setError(null);
        }

        if (password.isEmpty()) {
            et_password.setError("Password should not left blank");
            valid = false;
        } else {
            et_password.setError(null);

        }

        return valid;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) LoginActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {                                         //2?
                    Toast.makeText(this, "Do you want to Exit!", Toast.LENGTH_SHORT).show();


                    firstTime = secondTime;//firstTime
                    return true;
                } else {                                                    //2?
                    System.exit(0);
                }
                break;
        }

        return super.onKeyUp(keyCode, event);
    }

    private class LoadAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.e("inside onpre exe", "inside onpre exe");
            super.onPreExecute();
            progress = new ProgressDialog(LoginActivity.this);
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
                    nameValuePairs.add(new BasicNameValuePair("username", et_loginid.getText().toString() + ""));
                    nameValuePairs.add(new BasicNameValuePair("userpassword", et_password.getText().toString() + ""));

                    try {
                        DefaultHttpClient httpclient = new DefaultHttpClient();
                        HttpPost httppost = new HttpPost(Config.YOUR_SERVER_URL + "get_login_data.php");
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
                        result = sb.toString();
                        if (sb.toString().equalsIgnoreCase("null")) {
                            rslt = "Fail";
                        } else {
                            rslt = "";
                        }
                    } catch (Exception e) {
                        Log.e("log_tag", "Error converting result " + e.toString());
                    }

                    try {
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
                Toast.makeText(LoginActivity.this, "Invalid Login ID and Password", Toast.LENGTH_LONG).show();

            } else {
                User user = null;
                if (list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        user = list.get(i);
                        UId = user.UId;
                        UserName = user.UserName;
                    }
                    boolean tempcheck = checkBox.isChecked();
                    String tempstring = "";
                    if (tempcheck == true) {
                        tempstring = "1";
                    } else {
                        tempstring = "0";
                    }

                    prefs = getSharedPreferences(prefName, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("UId", "" + UId);
                    editor.putString("UserName", "" + UserName);
                    editor.putString("Remember", "" + tempstring);
                    editor.putString("Logout", "0");
                    editor.commit();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }

            }

        }

        protected void onProgressUpdate(Void... progress) {
            Log.e("inside progress exe", "inside progress exe");

        }
    }

    public void UserLoginDetail(String LoginId, String password) {
        list = DatabaseHelper.getUserLoginDetail(LoginId, password);
        User user = null;
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                user = list.get(i);
                UId = user.UId;
                UserName = user.UserName;
            }

            if (UId != 0) {
                prefs = getSharedPreferences(prefName, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("UId", "" + UId);
                editor.putString("UserName", "" + UserName);
                editor.commit();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.enter, R.anim.exit);
            } else {
                Toast.makeText(LoginActivity.this, "Invalid Login ID and Password", Toast.LENGTH_LONG).show();
            }

        }
    }
}
