package com.example.chetu.osrfinance.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.chetu.osrfinance.Activity.CustomerFinanceDetailActivity;
import com.example.chetu.osrfinance.Helper.DatabaseHelper;
import com.example.chetu.osrfinance.Model.CustomerFinance;
import com.example.chetu.osrfinance.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by chetu on 10/13/2016.
 */
public class Popup_Fragment extends DialogFragment implements View.OnClickListener {

    private OnListDialogItemSelect listener;
    private String title;
    private String[] list;
    TextView txt_amount;
    EditText et_date, et_penalty, et_remark;
    Button btn_yes;
    private AlphaAnimation alphaAnimation;
    private int mYear, mMonth, mDay;

    CustomerFinanceDetailActivity customerFinanceDetailActivity;
    String Amount = "", AmtDate = "";
    int CustId = 0;
    ArrayList<CustomerFinance> ln = new ArrayList<CustomerFinance>();
    List<String> categories3 = new ArrayList<String>();

    int listItemClick;

    public Popup_Fragment(CustomerFinanceDetailActivity listItemClick, String Amount, int CustId, String AmtDate) {
        this.customerFinanceDetailActivity = listItemClick;
        this.Amount = Amount;
        this.CustId = CustId;
        this.AmtDate = AmtDate;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(getActivity());
        View mView = mLayoutInflater.inflate(R.layout.popup_fragment_layout, null);
        DatabaseHelper.init(getActivity());
        alphaAnimation = new AlphaAnimation(3.0F, 0.4F);

        txt_amount = (TextView) mView.findViewById(R.id.txt_amount);
        et_date = (EditText) mView.findViewById(R.id.et_date);
        et_penalty = (EditText) mView.findViewById(R.id.et_penalty);
        et_remark = (EditText) mView.findViewById(R.id.et_remark);

        txt_amount.setText("" + Amount);

        et_date.setInputType(InputType.TYPE_NULL);
        et_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideKeyboard(v);
            }
        });

        et_date.setOnClickListener(this);

        btn_yes = (Button) mView.findViewById(R.id.btn_yes);
        // btn_no=(Button)mView.findViewById(R.id.btn_no);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                //ln.add(new CustomerFinance(""+et_date.getText().toString(),""+txt_amount.getText().toString(),""+et_penalty.getText().toString(),""+et_remark.getText().toString(),CustId));
                String temp = DatabaseHelper.insertCustomerFinanceDetail(ln);
                Toast.makeText(customerFinanceDetailActivity, "" + temp, Toast.LENGTH_LONG).show();
                customerFinanceDetailActivity.PopUpDataBinding();

            }
        });

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)

                .setCancelable(false)
                .setView(mView)
                .create();
        // alertDialog.getWindow().setLayout(600, 400);

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
            final DatePickerDialog dpd = new DatePickerDialog(customerFinanceDetailActivity,
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


                            et_date.setText(d + "/" + m + "/" + year);


                        }
                    }, mYear, mMonth, mDay);
            dpd.show();

            try {
                SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat format2 = new SimpleDateFormat("dd-MMM-yyyy");
                Date date = format1.parse("" + AmtDate);
                //Log.e("getDate()",""+format2.format(date));
                Date dtt = format2.parse("" + format2.format(date));

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
            }
        }
    }


    public interface OnListDialogItemSelect {
        public void onListItemSelected(String selection);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) customerFinanceDetailActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}