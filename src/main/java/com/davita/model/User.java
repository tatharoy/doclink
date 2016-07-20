package com.davita.model;

import com.davita.model.Content;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kmasood on 7/1/16.
 */
@Entity
public class User {
    @Id
    private String uid;
    private String name;

    @ElementCollection(targetClass=String.class, fetch = FetchType.EAGER)
//    @OneToMany(fetch = FetchType.EAGER)
    private List<String> readArticles = new ArrayList<String>();

    @OneToMany
    private List<Content> bookmarkedArticles = new ArrayList<Content>();

    @ElementCollection(targetClass=String.class, fetch = FetchType.EAGER)
//    @OneToMany
    private List<String> likedArticles = new ArrayList<String>();

    private User() {}

    public User(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public void addReadContent(String contentId) {
        readArticles.add(contentId);
    }

    public void addlikedContent(String contentId) { likedArticles.add(contentId); }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getReadArticles() {
        return readArticles;
    }

    public void setReadArticles(List<String> readArticles) {
        this.readArticles = readArticles;
    }

    public List<Content> getBookmarkedArticles() {
        return bookmarkedArticles;
    }

    public void setBookmarkedArticles(List<Content> bookmarkedArticles) {
        this.bookmarkedArticles = bookmarkedArticles;
    }

    public List<String> getLikedArticles() { return likedArticles; }

    public void setLikedArticles(List<String> likedArticles) {
        this.likedArticles = likedArticles;
    }

    @Override
    public String toString() {
        return String.format(
                "Customer[uid='%s', name='%s']",
                uid, name);
    }
}
