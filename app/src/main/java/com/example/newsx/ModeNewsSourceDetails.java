package com.example.newsx;

public class ModeNewsSourceDetails {

    String title,description,url,uriToImage,publishedAt,content;

    public ModeNewsSourceDetails(String title, String description, String url, String uriToImage, String publishedAt, String content) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.uriToImage = uriToImage;
        this.publishedAt = publishedAt;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUriToImage() {
        return uriToImage;
    }

    public void setUriToImage(String uriToImage) {
        this.uriToImage = uriToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
