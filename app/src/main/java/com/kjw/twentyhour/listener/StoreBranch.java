package com.kjw.twentyhour.listener;

public class StoreBranch implements OnStoreDataChangeListener{
    StoreData storeData;
    public String brandName;
    public String brandDescription;
    private String storeName;
    private String address;
    private double longitude;
    private double latitude;





    public StoreBranch(StoreData storeData , String storeName){
        this.storeData = storeData;
        this.storeName = storeName;
        storeData.registerObserver(this);
    }


    @Override
    public void update(String brandName, String brandDescription) {
        this.brandName = brandName;
        this.brandDescription = brandDescription;

    }

    @Override
    public void saveMongodb() {

    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }


}
