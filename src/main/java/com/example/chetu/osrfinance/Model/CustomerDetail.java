package com.example.chetu.osrfinance.Model;

/**
 * Created by chetu on 6/5/2018.
 */
public class CustomerDetail {
    public int CustId;
    public int CardNo;
    public String CustName;
    public String ContactNo;
    public String HomeAddress;
    public String BusiAddress;
    public String Amount;
    public String DailyAmt;
    public String AmtDate;
    public String GuarantorOrIntroducerName;
    public String GContactNo;
    public String PhotoPath;
    public int UserId;


    public CustomerDetail(int custId, int cardNo, String custName, String contactNo, String homeAddress, String busiAddress, String amount, String dailyAmt, String amtDate, String guarantorOrIntroducerName, String GContactNo, String photoPath, int UserId) {
        this.CustId = custId;
        this.CardNo = cardNo;
        this.CustName = custName;
        this.ContactNo = contactNo;
        this.HomeAddress = homeAddress;
        this.BusiAddress = busiAddress;
        this.Amount = amount;
        this.DailyAmt = dailyAmt;
        this.AmtDate = amtDate;
        this.GuarantorOrIntroducerName = guarantorOrIntroducerName;
        this.GContactNo = GContactNo;
        this.PhotoPath = photoPath;
        this.UserId = UserId;
    }
}
