package com.kjw.twentyhour.listener;

import android.content.res.Resources;
import com.kjw.twentyhour.R;

import java.util.ArrayList;

public class StoreData implements Subject {

    private ArrayList observers;
    String brandName = "비엔나 커피 하우스, Wien1683";
    String brandDescription = "유네스코 무형문화유산에 등록된,300년 전통의 역사와 문화를 간직한 비엔나커피하우스로 초대합니다";


    public StoreData(){

        observers = new ArrayList();

    }

    @Override
    public void registerObserver(OnStoreDataChangeListener o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(OnStoreDataChangeListener o) {
        int i = observers.indexOf(o);
        if(i >= 0){
            observers.remove(i);
        }
    }

    @Override
    public void notifyObservers() {
        for(int i = 0; i < observers.size(); i++){
            OnStoreDataChangeListener observer = (OnStoreDataChangeListener)observers.get(i);
            observer.update(brandName,brandDescription);
        }
    }

    public void storeDataChange(){
        notifyObservers();
    }

    public void setChangeStoreData(){

        this.brandDescription = brandDescription;
        this.brandName = brandName;
    }


}
