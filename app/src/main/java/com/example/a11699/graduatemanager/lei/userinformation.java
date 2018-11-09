package com.example.a11699.graduatemanager.lei;

public class userinformation {
    private String unit;//公司
    private String name;//
    private String job;
    private String tutor;//导师
    private String qiandaoCount;//签到次数
    private String tPhone;//老师电话
    private String picture;//学生图片
    private String city;//实习地点
    public userinformation(String unit, String name, String job, String tutor, String qiandaoCount,String tPhone,String picture,String city) {
        this.unit = unit;
        this.name = name;
        this.job = job;
        this.tutor = tutor;
        this.qiandaoCount = qiandaoCount;
        this.tPhone=tPhone;
        this.picture=picture;
        this.city=city;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String gettPhone() {
        return tPhone;
    }

    public void settPhone(String tPhone) {
        this.tPhone = tPhone;
    }

    public String getUnit() {
        return unit;
    }

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }

    public String getTutor() {
        return tutor;
    }

    public String getQiandaoCount() {
        return qiandaoCount;
    }
}
