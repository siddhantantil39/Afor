package com.example.afor;

public class answer_recieve {

    //model class
    String name;
    String imageUrl;

    //constructors

    public answer_recieve() {
    }

    public answer_recieve(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }



    //getters and setters
    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getImageUrl() {

        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {

        this.imageUrl = imageUrl;
    }

}
