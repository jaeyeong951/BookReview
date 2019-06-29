package com.example.bookreviewer;

public class itemObject_detail {
    private String title;
    private String img_url;
    private String Site_name;
    private String author;
    private Float star;
    private String price;
    private String review;
    private String Url;

    public itemObject_detail(String title, String url, String Site_name, String author, Float star, String price, String review, String Url){
        this.title = title;
        this.img_url = url;
        this.Site_name = Site_name;
        this.author = author;
        this.star = star;
        this.price = price;
        this.review = review;
        this.Url = Url;
    }

    public String getTitle(){
        return title;
    }

    public String getImg_url(){
        return img_url;
    }

    public String getSite_name(){
        return Site_name;
    }

    public String getAuthor(){
        return author;
    }

    public Float getStar(){
        return star;
    }

    public String getPrice() { return price; }

    public String getReview() { return review; }

    public String getUrl() { return Url; }
}