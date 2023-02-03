package com.example.inshta.Models;

public class Users {

    private String name , email , password;
    private String coverPhoto;
    private String profile;
    private String userId;
    private int followerCount;
    private String profession2;


    public Users(String name,String profession2, String email, String password) {
        this.name = name;
        this.profession2 = profession2;
        this.email = email;
        this.password = password;
    }

    public Users() {
    }

    public String getProfession2() {
        return profession2;
    }

    public void setProfession2(String profession2) {
        this.profession2 = profession2;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
