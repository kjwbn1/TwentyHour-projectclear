package com.kjw.twentyhour.factory;

import com.kjw.twentyhour.listener.StoreBranch;
import com.kjw.twentyhour.listener.StoreData;

import java.util.ArrayList;
import java.util.List;

public class StoreFactory {

    ArrayList<StoreBranch> storeFranches = new ArrayList<>();


    public StoreBranch createStoreBranch(int j, StoreData storeData) {

        switch (j){
            case 0:
                StoreBranch sb1 = new StoreBranch(storeData, "");
                storeFranches.add(sb1);
                return sb1;
            case 1:
                StoreBranch sb2 = new StoreBranch(storeData, "");
                storeFranches.add(sb2);
                return sb2;
            case 2:
                StoreBranch sb3 = new StoreBranch(storeData, "");
                storeFranches.add(sb3);
                return sb3;
            case 3:
                StoreBranch sb4 = new StoreBranch(storeData, "");
                storeFranches.add(sb4);
                return sb4;
            case 4:
                StoreBranch sb5 = new StoreBranch(storeData, "");
                storeFranches.add(sb5);
                return sb5;

            case 5:
                StoreBranch sb6 = new StoreBranch(storeData, "");
                storeFranches.add(sb6);
                return sb6;

            case 6:
                StoreBranch sb7 = new StoreBranch(storeData, "");
                storeFranches.add(sb7);
                return sb7;
        }
        return null;
    }

    public ArrayList<StoreBranch> getStoreFranches() {
        return storeFranches;
    }

    public void setStoreFranches(ArrayList<StoreBranch> storeFranches) {
        this.storeFranches = storeFranches;
    }
}
