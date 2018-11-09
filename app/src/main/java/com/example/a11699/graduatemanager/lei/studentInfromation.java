package com.example.a11699.graduatemanager.lei;

public class studentInfromation {
    private String name;
   private String cid;
    public studentInfromation(String name) {
        this.name = name;
    }

    public studentInfromation(String name, String cid) {
        this.name = name;
        this.cid = cid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
