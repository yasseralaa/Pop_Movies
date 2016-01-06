package com.example.yasseralaaeldin.phase2.classes;

import java.io.Serializable;

/**
 * Created by Yasser Alaa Eldin on 12/24/2015.
 */
public class Trailers  implements Serializable {
    String key;
    String name;
    String site;
    String size;
    String type;
    String id;
    int DBid;
    public static String share;

    public Trailers(){}


    public Trailers(String key, String name, String site, String size, String type  ,String id) {
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDBid() {
        return DBid;
    }

    public void setDBid(int DBid) {
        this.DBid = DBid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
