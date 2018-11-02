package com.kjw.twentyhour.data;

import java.io.Serializable;

public class Food implements Serializable {
    private String foodName;
    private int foodImage = 0;
    private String price;
    private String description;


    public Food(String foodName, String price) {
    this.foodName = foodName;
    this.price = price;
    }


    public Food(String foodName, int foodImage, String price , String description){
        this.foodName = foodName;
        this.foodImage = foodImage;
        this.price = price;
        this.description = description;
    }

    public String getFoodName() {
        return foodName;
    }

    public int getFoodImage() {
        return foodImage;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription(){

        return description;
    }




}
