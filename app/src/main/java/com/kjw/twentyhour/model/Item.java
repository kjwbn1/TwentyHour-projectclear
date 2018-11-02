package com.kjw.twentyhour.model;


public class Item {

    private String address;
    private Boolean isAdmAddress = true;
    private Boolean isRoadAddress = true;

    /**
     * No args constructor for use in serialization
     *
     */
    public Item() {
    }

    /**
     *
     * @param isAdmAddress
     * @param address
     * @param isRoadAddress
     */
    public Item(String address, Boolean isAdmAddress, Boolean isRoadAddress) {
        super();
        this.address = address;
        this.isAdmAddress = isAdmAddress;
        this.isRoadAddress = isRoadAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



    public Boolean getIsAdmAddress() {
        return isAdmAddress;
    }

    public void setIsAdmAddress(Boolean isAdmAddress) {
        this.isAdmAddress = isAdmAddress;
    }

    public Boolean getIsRoadAddress() {
        return isRoadAddress;
    }

    public void setIsRoadAddress(Boolean isRoadAddress) {
        this.isRoadAddress = isRoadAddress;
    }



}
