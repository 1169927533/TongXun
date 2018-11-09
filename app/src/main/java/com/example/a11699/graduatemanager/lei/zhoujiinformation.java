package com.example.a11699.graduatemanager.lei;

public class zhoujiinformation {
    private String date;
    private String review;
    private String detail;
    private String title;
    private String cid;
    private String reviewDetail;
    private String image;

    public zhoujiinformation(String date, String review, String detail, String title, String cid) {
        this.date = date;
        this.review = review;
        this.detail = detail;
        this.title = title;
        this.cid = cid;

    }
    public zhoujiinformation(String reviewDetail,String date, String review, String detail, String title, String cid) {
        this.date = date;
        this.review = review;
        this.detail = detail;
        this.title = title;
        this.cid = cid;
         this.reviewDetail=reviewDetail;
    }
    public zhoujiinformation(String reviewDetail,String date, String review, String detail, String title, String cid,String image) {
        this.date = date;
        this.review = review;
        this.detail = detail;
        this.title = title;
        this.cid = cid;
        this.reviewDetail=reviewDetail;
        this.image=image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getReviewDetail() {
        return reviewDetail;
    }

    public void setReviewDetail(String reviewDetail) {
        this.reviewDetail = reviewDetail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}
