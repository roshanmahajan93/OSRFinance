package com.example.chetu.osrfinance.Model;

/**
 * Created by chetu on 6/3/2018.
 */
public class User {
    public int UId;
    public String UserName;
    public String UserPassword;
    public String CompanyName;

    public User(int UId, String userName, String userPassword, String companyName) {
        this.UId = UId;
        this.UserName = userName;
        this.UserPassword = userPassword;
        this.CompanyName = companyName;
    }
}
