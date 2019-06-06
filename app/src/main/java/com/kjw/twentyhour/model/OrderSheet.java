package com.kjw.twentyhour.model;

import java.util.ArrayList;
import java.util.List;

public class OrderSheet {

    public Integer totalPrice;
    public String cardNumber;
    public String name;
    public String customerEmail;
    public List<Product> product = new ArrayList<>();


}


//    public String getTotalPrice() {
//        return totalPrice;
//    }
//
//    public void setTotalPrice(String totalPrice) {
//        this.totalPrice = totalPrice;
//    }
//
//    public String getCardNumber() {
//        return cardNumber;
//    }
//
//    public void setCardNumber(String cardNumber) {
//        this.cardNumber = cardNumber;
//    }
//
//    public String getCustomerEmail() {
//        return customerEmail;
//    }
//
//    public void setCustomerEmail(String customerEmail) {
//        this.customerEmail = customerEmail;
//    }
//
//    public String getCustomerName() {
//        return customerName;
//    }
//
//    public void setCustomerName(String customerName) {
//        this.customerName = customerName;
//    }
//
//    public String getQuantity() {
//        return quantity;
//    }
//
//    public void setQuantity(Product product) {
//        this.quantity = product.getQuantity();
//    }
//
//    public String getProductName() {
//        return productName;
//    }
//
//    public void setProductName(Product product) {
//        this.productName = product.getProduct();
//    }

//product: { productName: productName, quantity: quantity },
//        customName: customerName,
//        customEmail: customerEmail,
//        totalPrice: totalprice