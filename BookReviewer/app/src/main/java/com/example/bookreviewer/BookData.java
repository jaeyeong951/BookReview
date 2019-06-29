package com.example.bookreviewer;

public class BookData {
    private String title;
    private String Author;
    private int resId;
    private String price;
    private String review;
    private String site_name;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return Author;
    }
    public void setAuthor(String content) {
        this.Author = content;
    }

    public int getResId() {
        return resId;
    }
    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }

    public String getSite_name() {
        return site_name;
    }
    public void setSite_name(String site_name) {
        this.site_name = site_name;
    }

    public String getReview() {
        return review;
    }
    public void setReview(String review) {
        this.review = review;
    }

}