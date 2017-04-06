package com.example.v_ruchd.capstonestage2;

import com.example.v_ruchd.capstonestage2.modal.Sources;

import java.util.HashMap;
import java.util.List;

/**
 * Created by v-ruchd on 4/4/2017.
 */

public class SingletonClass {
static SingletonClass singletonClass;
private HashMap<String,List<Sources>> mSources=new HashMap<>();
    private SingletonClass(){

    }


    public static SingletonClass getInstance(){
if(singletonClass==null){
    singletonClass=new SingletonClass();
}
        return singletonClass;

    }


    public void setData(String selectedData,List<Sources> sourcesList){
        mSources.put(selectedData,sourcesList);
    }


    public List<Sources> getData(String selectedData){
       return mSources.get(selectedData);
    }
}
