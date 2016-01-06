package com.example.yasseralaaeldin.phase2.classes;

import java.io.Serializable;

/**
 * Created by Yasser Alaa Eldin on 12/25/2015.
 */
public class Reviews implements Serializable {
    String author;
    String content;
    String id;
    int DBid;

    public Reviews(){}

    public Reviews(String author, String content ,String id) {
        this.author = author;
        this.content = content;
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
