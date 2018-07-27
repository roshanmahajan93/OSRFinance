package com.example.chetu.osrfinance.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.example.chetu.osrfinance.R;


public class SplashScreenActivity extends Activity {
    public static final String MyPREFERENCES = "UserDetails";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        Thread t = new Thread() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                super.run();
                try {
                    sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    String id = sharedpreferences.getString("UserName", "");
                    String Remember = sharedpreferences.getString("Remember", "");
                    Log.e("id", "SplashSreen----->" + id);

                    if (id.equalsIgnoreCase("") || Remember.equalsIgnoreCase("0")) {
                        Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                }
            }


        };
        t.start();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }
}