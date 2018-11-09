package com.example.a11699.graduatemanager.lei;

import java.io.Serializable;

public class dabianInformation implements Serializable {

    public String date,reviewDetail,review,tName,rid,title,tid,sid,quession;

    public dabianInformation(String date, String reviewDetail, String review, String tName, String rid, String title, String tid, String sid,String quession) {
        this.date = date;
        this.reviewDetail = reviewDetail;
        this.review = review;
        this.tName = tName;
        this.rid = rid;
        this.title = title;
        this.tid = tid;
        this.sid = sid;
        this.quession=quession;
    }

    public String getQuession() {
        return quession;
    }

    public void setQuession(String quession) {
        this.quession = quession;
    }

    public String getDate() {
        return date;
    }

    public String getReviewDetail() {
        return reviewDetail;
    }

    public String getReview() {
        return review;
    }

    public String gettName() {
        return tName;
    }

    public String getRid() {
        return rid;
    }

    public String getTitle() {
        return title;
    }

    public String getTid() {
        return tid;
    }

    public String getSid() {
        return sid;
    }
}
