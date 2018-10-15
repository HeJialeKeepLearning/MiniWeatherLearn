package com.example.hejiale.bean;

import android.media.Image;

public class TodayWeather {
    private String city;
    private String updatetime;
    private String temperature;
    private String moist;
    private String pm25;
    private String quality;
    private String winddir;
    private String windpower;
    private String date;
    private String high;
    private String low;
    private String weather;

    public String getCity() {
        return city;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getDate() {
        return date;
    }

    public String getHigh() {
        return high;
    }

    public String getLow() {
        return low;
    }

    public String getMoist() {
        return moist;
    }

    public String getPm25() {
        return pm25;
    }

    public String getQuality() {
        return quality;
    }

    public String getWeather() {
        return weather;
    }

    public String getWinddir() {
        return winddir;
    }

    public String getWindpower() {
        return windpower;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public void setMoist(String moist) {
        this.moist = moist;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public void setWinddir(String winddir) {
        this.winddir = winddir;
    }

    public void setWindpower(String windpower) {
        this.windpower = windpower;
    }

    @Override
    public String toString(){
        return "TodayWeather" +
                "city='" + city + '\'' +
                ",updatetime='" + updatetime+'\''+
                ",temperature='"+temperature+'\''+
                ",moist='"+moist+'\''+
                ",pm25='"+pm25+'\''+
                ",quality='"+quality+'\''+
                ",winddir='"+winddir+'\''+
                ",windpower="+windpower+'\''+
                ",date='"+date+'\''+
                ",high='"+high+'\''+
                ",low='"+low+'\''+
                ",weather='"+weather+'\''+
                '}';
    }
}
