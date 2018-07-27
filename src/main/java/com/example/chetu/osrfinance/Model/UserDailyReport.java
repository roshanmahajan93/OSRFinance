package com.example.chetu.osrfinance.Model;

/**
 * Created by Vinayak on 7/10/2018.
 */

public class UserDailyReport {
    public String card_no;
    public String CustName;
    public String Amount;
    public String user_id;
    public String user_name;
    public String UDate;
    public String SDate;
    public String total_amount;


    public UserDailyReport(String card_no, String custName, String amount, String user_id, String user_name, String UDate, String SDate, String total_amount) {
        this.card_no = card_no;
        this.CustName = custName;
        this.Amount = amount;
        this.user_id = user_id;
        this.user_name = user_name;
        this.UDate = UDate;
        this.SDate = SDate;
        this.total_amount = total_amount;
    }
}
