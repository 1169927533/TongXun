package com.example.a11699.graduatemanager.lei;

import java.util.List;

public class telPeople {
    public List<String> telList;
    private String name;
    private String sid;
   private String picture_url;

    public telPeople(String name, String sid) {
        this.name = name;
        this.sid = sid;
    }

    public telPeople(String name, String sid, String picture_url) {
        this.name = name;
        this.sid = sid;
        this.picture_url = picture_url;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }
}
