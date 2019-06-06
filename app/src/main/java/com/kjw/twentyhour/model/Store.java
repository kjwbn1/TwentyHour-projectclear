package com.kjw.twentyhour.model;

import android.content.res.Resources;
import com.kjw.twentyhour.R;

import java.util.ArrayList;

public class Store {

    private String storeName;
    private String address;
    private float longitude;
    private float latitude;

    public String storeName() {
        return storeName;
    }

    public void storeName(String storeNamebyLocation) {
        this.storeName = storeNamebyLocation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }
}
