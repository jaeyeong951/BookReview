package com.example.bookreviewer;

public class reviewObject {
    private String date;
    private String name;
    private String review;
    private Float rating;

    public reviewObject(String date, String name, String review, Float rating){
        this.date = date;
        this.name = name;
        this.review = review;
        this.rating = rating;
    }


    public String getDate(){
        return date;
    }
    public String getName(){
        return name;
    }
    public String getReview(){
        return review;
    }
    public Float getRating(){
        return rating;
    }

}
