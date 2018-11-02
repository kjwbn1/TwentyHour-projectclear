package com.kjw.twentyhour.model;



public class Status {

    private String email;
    private String address;
    private String day;
    private String timestart;
    private String timeend;
    private String created_at;


    public void setEmail(String email){
        this.email = email;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public void setDay(String day){
        this.day = day;
    }

    public void setTimeS(String timestart){
        this.timestart = timestart;
    }

    public void setTimeE(String timeend){
        this.timeend = timeend;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getEmail(){
        return email;
    }

    public String getAddress(){
        return address;
    }

    public String getDay(){
        return day;
    }

    public String getTimeS(){
        return timestart;
    }

    public String getTimeE(){
        return timeend;
    }






}
