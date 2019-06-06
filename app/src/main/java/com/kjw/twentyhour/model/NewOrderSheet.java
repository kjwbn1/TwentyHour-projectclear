package com.kjw.twentyhour.model;



import java.util.List;

public class NewOrderSheet {
    public List<NewProduct> newProductList;
    public String totalPrice;
    public String employee;
    public String uploadDate;

    public NewOrderSheet(List<NewProduct> newProductList, String employee, String uploadDate, String totalPrice){
        this.newProductList = newProductList;
        this.totalPrice = totalPrice;
        this.employee = employee;
        this.uploadDate = uploadDate;
    }




}
