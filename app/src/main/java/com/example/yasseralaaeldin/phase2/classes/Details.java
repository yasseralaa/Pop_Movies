package com.example.yasseralaaeldin.phase2.classes;

import java.io.Serializable;

/**
 * Created by Yasser Alaa Eldin on 12/8/2015.
 */
public class Details implements Serializable {
    String title;
    String img_path;
    String date;
    String rate;
    String overview;
    String id;
    int DBid;



    public Details(){}

    public Details(String title, String img_path, String date, String rate, String overview,String id) {
        this.title = title;
        this.img_path = img_path;
        this.date = date;
        this.rate = rate;
        this.overview = overview;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDBid() {
        return DBid;
    }

    public void setDBid(int DBid) {
        this.DBid = DBid;
    }

}
