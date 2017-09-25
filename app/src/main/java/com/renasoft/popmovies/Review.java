package com.renasoft.popmovies;

/**
 * Created by BTSC-2 on 20/04/2016.
 */
public class Review {
    private String id;
    private String author;
    private String content;

    public Review(String id, String author, String content) {
        this.id = id;
        this.author = author;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }
}
