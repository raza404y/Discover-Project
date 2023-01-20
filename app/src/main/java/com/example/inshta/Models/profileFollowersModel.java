package com.example.inshta.Models;

public class profileFollowersModel {
    int followerPicture;
    String followerName;

    public profileFollowersModel(int followerPicture, String followerName) {
        this.followerPicture = followerPicture;
        this.followerName = followerName;
    }

    public int getFollowerPicture() {
        return followerPicture;
    }

    public void setFollowerPicture(int followerPicture) {
        this.followerPicture = followerPicture;
    }

    public String getFollowerName() {
        return followerName;
    }

    public void setFollowerName(String followerName) {
        this.followerName = followerName;
    }
}
