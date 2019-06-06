package com.kjw.twentyhour.Franchise;

import com.kjw.twentyhour.model.Store;

public class ConcreteStore extends Store {

    private String storeName;
    private String address;
    private long longitude;
    private long latitude;

    public ConcreteStore(String address , String storeName, long longitude, long latitude){
        this.address = address;
        this.storeName = storeName;
        this.longitude = longitude;
        this.latitude = latitude;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

}


