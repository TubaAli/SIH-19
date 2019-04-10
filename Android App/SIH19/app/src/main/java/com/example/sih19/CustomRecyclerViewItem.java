package com.example.sih19;

public class CustomRecyclerViewItem {

    private String text;
    private int imageId;
    private String newsLink;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getBackgroundId() {
        return imageId;
    }

    public void setBackgroundId(int imageId) {
        this.imageId = imageId;
    }

    public String getNewsLink() {
        return newsLink;
    }

    public void setNewsLink(String newsLink) {
        this.newsLink = newsLink;
    }
}