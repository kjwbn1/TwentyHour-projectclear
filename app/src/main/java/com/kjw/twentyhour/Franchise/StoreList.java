package com.kjw.twentyhour.Franchise;

import java.util.ArrayList;
import java.util.List;

public class StoreList {


    List<ConcreteStore> stores = new ArrayList<>();

    public StoreList(){

    }

    public void addStore(String storeName, String address, long longitude , long latitude){

        ConcreteStore concreteStore = new ConcreteStore(storeName, address, longitude, latitude);

        stores.add(concreteStore);
    }






}
