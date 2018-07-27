package com.example.chetu.osrfinance.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chetu.osrfinance.R;
import com.example.chetu.osrfinance.Adapter.ImageAdapter;

public class MainActivity extends Activity {
    static final String[] MOBILE_OS = new String[]{
            "New Customer", "Edit Customer", "Search Customer", "Customer Daily Entry", "2 days Report", "50 Day Complete Report", "Delete Account Detail", "Daily Entry Report", "Delete Last Report"};

    private long firstTime = 0;

    private TextView textView3;
    private GridView gridView;
    private ImageButton imgBtn_logout;
    private Context _context = this;

    private SharedPreferences prefs;
    private String prefName = "UserDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        String customFont = "fonts/arlrdbd.ttf";
        Typeface typeface = Typeface.createFromAsset(getAssets(), customFont);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView3.setTypeface(typeface);
        imgBtn_logout = (ImageButton) findViewById(R.id.imgBtn_logout);

        imgBtn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        _context);

                // set title
                alertDialogBuilder.setTitle("LogOut");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Are you Sure?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                prefs = MainActivity.this.getSharedPreferences(prefName, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("Logout", "1");

                                editor.commit();

                                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(i);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        });

        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(new ImageAdapter(this, MOBILE_OS));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {


                TextView tv;
                tv = (TextView) v.findViewById(R.id.grid_item_label);
                String selectitem;

                selectitem = tv.getText().toString();

                if (selectitem.equals("New Customer")) {

                    Intent i = new Intent(getBaseContext(), CustomerDetailActivity.class);
                    i.putExtra("ActivityName", "NewCustomer");
                    startActivity(i);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }

                if (selectitem.equals("Edit Customer")) {

                    Intent i = new Intent(getBaseContext(), SearchListActivity.class);
                    i.putExtra("ActivityName", "EditCustomer");
                    startActivity(i);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }

                if (selectitem.equals("Search Customer")) {

                    Intent i = new Intent(getBaseContext(), SearchListActivity.class);
                    i.putExtra("ActivityName", "SearchCustomer");
                    startActivity(i);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }

                if (selectitem.equals("Customer Daily Entry")) {
                    Intent i = new Intent(getBaseContext(), DailyCustomerFinanceEntryActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }

                if (selectitem.equals("2 days Report")) {
                    Intent i = new Intent(getBaseContext(), TwoDaysAndFiftyDaysReportActivity.class);
                    i.putExtra("ActivityName", "2daysReport");
                    startActivity(i);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }

                if (selectitem.equals("50 Day Complete Report")) {
                    Intent i = new Intent(getBaseContext(), TwoDaysAndFiftyDaysReportActivity.class);
                    i.putExtra("ActivityName", "50daysReport");
                    startActivity(i);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }

                if (selectitem.equals("Delete Account Detail")) {
                    Intent i = new Intent(getBaseContext(), DeleteCustomerDetailActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }

                if (selectitem.equals("Daily Entry Report")) {
                    Intent i = new Intent(getBaseContext(), DailyEntryReportActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }

                if (selectitem.equals("Delete Last Report")) {
                    Intent i = new Intent(getBaseContext(), DeleteCustomerLastRecordActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }

                /*if (selectitem.equals("LogOut"))
                {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            _context);

                    // set title
                    alertDialogBuilder.setTitle("LogOut");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Are you Sure?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, close
                                    // current activity
//                                    MainActivity.this.finish();
                                    sqLiteDatabase.execSQL("delete from "+ TABLE_NAME);

                                    Login.server = 72;
                                    Intent i = new Intent(getApplicationContext(),Login.class);
                                    startActivity(i);
                                    finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }*/
            }
        });

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
                } else {
                    System.exit(0);
                    finish();
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }
}
