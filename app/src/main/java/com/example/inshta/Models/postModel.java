package com.example.inshta.Models;

public class postModel {

    int profileImage , postImage ;

    String postUsername , postProfession , likes , comments , shares;

    public postModel(int profileImage, int postImage, String postUsername, String postProfession, String likes, String comments, String shares) {
        this.profileImage = profileImage;
        this.postImage = postImage;
        this.postUsername = postUsername;
        this.postProfession = postProfession;
        this.likes = likes;
        this.comments = comments;
        this.shares = shares;
    }

    public int getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(int profileImage) {
        this.profileImage = profileImage;
    }

    public int getPostImage() {
        return postImage;
    }

    public void setPostImage(int postImage) {
        this.postImage = postImage;
    }

    public String getPostUsername() {
        return postUsername;
    }

    public void setPostUsername(String postUsername) {
        this.postUsername = postUsername;
    }

    public String getPostProfession() {
        return postProfession;
    }

    public void setPostProfession(String postProfession) {
        this.postProfession = postProfession;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getShares() {
        return shares;
    }

    public void setShares(String shares) {
        this.shares = shares;
    }
}

