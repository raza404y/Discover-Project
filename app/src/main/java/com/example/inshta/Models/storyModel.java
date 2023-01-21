package com.example.inshta.Models;

public class storyModel {
    int story , storyProfileImage;

    public storyModel(int story, int storyProfileImage) {
        this.story = story;
        this.storyProfileImage = storyProfileImage;
    }

    public int getStory() {
        return story;
    }

    public void setStory(int story) {
        this.story = story;
    }

    public int getStoryProfileImage() {
        return storyProfileImage;
    }

    public void setStoryProfileImage(int storyProfileImage) {
        this.storyProfileImage = storyProfileImage;
    }
}
