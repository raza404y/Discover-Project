package com.example.inshta.Models;

public class postModel {
    private String postId;
    private String postedBy;
    private long postAt;
    private String postDescription;
    private String postImage;


    public postModel(String postId, String postedBy, long postAt, String postDescription, String postImage) {
        this.postId = postId;
        this.postedBy = postedBy;
        this.postAt = postAt;
        this.postDescription = postDescription;
        this.postImage = postImage;
    }

    public postModel() {
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public long getPostAt() {
        return postAt;
    }

    public void setPostAt(long postAt) {
        this.postAt = postAt;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }
}

