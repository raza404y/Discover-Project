package com.example.inshta.Models;

public class profileFollowersModel {

    private String followedBy;
    private long followerdAt;

    public profileFollowersModel(String followedBy, long followerdAt) {
        this.followedBy = followedBy;
        this.followerdAt = followerdAt;
    }

    public profileFollowersModel() {
    }

    public String getFollowedBy() {
        return followedBy;
    }

    public void setFollowedBy(String followedBy) {
        this.followedBy = followedBy;
    }

    public long getFollowerdAt() {
        return followerdAt;
    }

    public void setFollowerdAt(long followerdAt) {
        this.followerdAt = followerdAt;
    }
}
