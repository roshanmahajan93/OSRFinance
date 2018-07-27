package com.example.chetu.osrfinance.Model;

/**
 * Created by Vinayak on 7/2/2018.
 */

public class CustomerTwoAndFiftyReport {
    public String CardNo;
    public String CustName;
    public String ContactNo;
    public String No_Of_Day_Pending;
    public String DailyAmt;
    public String Amount;
    public String GuarantorOrIntroducerName;
    public String GContactNo;

    public CustomerTwoAndFiftyReport(String cardNo, String custName, String contactNo, String no_Of_Day_Pending, String dailyAmt, String amount, String guarantorOrIntroducerName, String GContactNo) {
        this.CardNo = cardNo;
        this.CustName = custName;
        this.ContactNo = contactNo;
        this.No_Of_Day_Pending = no_Of_Day_Pending;
        this.DailyAmt = dailyAmt;
        this.Amount = amount;
        this.GuarantorOrIntroducerName = guarantorOrIntroducerName;
        this.GContactNo = GContactNo;
    }
}
