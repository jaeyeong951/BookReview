package com.example.bookreviewer;

public class ItemObject {

    private String img_url;
    private String URL;
    private String author;
    private Float star;
    private String title;
    private String price;
    private String siteName;

    public ItemObject(String url, String site_url, String author, Float star, String title, String price, String siteName){
        this.img_url = url;
        this.URL = site_url;
        this.author = author;
        this.star = star;
        this.title = title;
        this.price = price;
        this.siteName = siteName;
    }


    public String getImg_url(){
        return img_url;
    }
    public String getUrl() { return URL; }
    public String getAuthor() { return author; }
    public Float getStar() { return star; }
    public String getTitle() { return title; }
    public String getPrice() { return price; }
    public String getSiteName() { return siteName; }


}

