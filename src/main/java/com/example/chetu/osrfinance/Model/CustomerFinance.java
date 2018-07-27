package com.example.chetu.osrfinance.Model;

/**
 * Created by Vinayak on 6/6/2018.
 */

public class CustomerFinance {
    public int CustFinId;
    public String FDate;
    public String FAmount;
    public String FineAndPenalty;
    public String FRemark;
    public int CustId;

    public CustomerFinance(int custFinId, String FDate, String FAmount, String fineAndPenalty, String FRemark, int custId) {
        this.CustFinId = custFinId;
        this.FDate = FDate;
        this.FAmount = FAmount;
        this.FineAndPenalty = fineAndPenalty;
        this.FRemark = FRemark;
        this.CustId = custId;
    }

    public CustomerFinance(String FDate, String FAmount, String fineAndPenalty, String FRemark) {
        this.FDate = FDate;
        this.FAmount = FAmount;
        this.FineAndPenalty = fineAndPenalty;
        this.FRemark = FRemark;
    }
}
