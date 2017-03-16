package com.wolff.wshablon.objects;

import java.io.Serializable;

/**
 * Created by wolff on 15.03.2017.
 */

public class WItem implements Serializable{
    private static final long serialVersionUID = 2163051469151704486L;
    private String pictureName;
    private int minTemperature;
    private int maxTemperature;
    private WSeasons season;
    private  String name;

    public WItem(String name,WSeasons season,int minTemperature,int maxTemperature,String pictureName){
        this.name = name;
        this.season = season;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.pictureName =pictureName;

    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public int getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(int minTemperature) {
        this.minTemperature = minTemperature;
    }

    public int getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(int maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public WSeasons getSeason() {
        return season;
    }

    public void setSeason(WSeasons season) {
        this.season = season;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
