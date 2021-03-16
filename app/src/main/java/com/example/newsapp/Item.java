package com.example.newsapp;

import android.graphics.Bitmap;

public class Item {
    private String title;
    private String description;
    private String content;
    private String url;
    private Bitmap image;
    private String author;
    private String date;

    public Item(String title, String description, String content, String author, String date, String url, Bitmap image)
    {
        this.title = title;
        this.description = description;
        this.content = content;
        this.url = url;
        this.image = image;
        this.author = author;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() { return url; }

    public Bitmap getImage() { return image; }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

}

