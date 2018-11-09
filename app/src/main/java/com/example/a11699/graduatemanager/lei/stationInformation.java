package com.example.a11699.graduatemanager.lei;

public class stationInformation {

    private String city;//ci市
    private String district;//区
    private String latitude;//经度
    private String longitude;//纬度
    private String qiandao;

    public stationInformation(String city, String district, String latitude, String longitude) {
        this.city = city;
        this.district = district;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public stationInformation(String city, String district, String latitude, String longitude,String qiandao) {
        this.city = city;
        this.district = district;
        this.latitude = latitude;
        this.longitude = longitude;
        this.qiandao=qiandao;
    }
    public String getQiandao() {
        return qiandao;
    }

    public void setQiandao(String qiandao) {
        this.qiandao = qiandao;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
