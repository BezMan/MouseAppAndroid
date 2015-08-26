package com.mouse.world.model;

/**
 * Created by betzalel on 30/03/2015.
 */
public class ListModel {
    private  String TitleA="";
    private String TitleB="";
    private String TitleC="";
    private String Address="";
    private  int Price;
    private  float Rating;
    private String ObjId;
    private String NsId;
    private String Distance = "0 מטרים ממך ";

    public String getNsId() {
        return NsId;
    }

    public void setNsId(String nsId) {
        NsId = nsId;
    }

    public String getObjId() {
        return ObjId;
    }

    public void setObjId(String objId) {
        ObjId = objId;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public String getTitleA() {
        return TitleA;
    }

    public void setTitleA(String titleA) {
        TitleA = titleA;
    }

    public String getTitleB() {
        return TitleB;
    }

    public void setTitleB(String titleB) {
        TitleB = titleB;
    }

    public String getTitleC() {
        return TitleC;
    }

    public void setTitleC(String titleC) {
        TitleC = titleC;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public float getRating() {
        return Rating;
    }

    public void setRating(float rating) {
        Rating = rating;
    }


}

