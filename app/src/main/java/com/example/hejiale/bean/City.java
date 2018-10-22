package com.example.hejiale.bean;

public class City {
    private String province;
    private String city;
    private String number;
    private String firPY;
    private String allPY;
    private String allfirPY;

    public City(String province,String city,String number,String allPY,String allfirPY,String firPY){
        this.province=province;
        this.city=city;
        this.number=number;
        this.firPY=firPY;
        this.allPY=allPY;
        this.allfirPY=allfirPY;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAllfirPY() {
        return allfirPY;
    }

    public String getAllPY() {
        return allPY;
    }

    public String getFirPY() {
        return firPY;
    }

    public String getNumber() {
        return number;
    }

    public String getProvince() {
        return province;
    }

    public void setAllfirPY(String allfirPY) {
        this.allfirPY = allfirPY;
    }

    public void setAllPY(String allPY) {
        this.allPY = allPY;
    }

    public void setFirPY(String firPY) {
        this.firPY = firPY;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
