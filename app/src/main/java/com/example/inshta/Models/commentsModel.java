package com.example.inshta.Models;

public class commentsModel {
    String commentText;
    long commentedAt;
    String commentedBy;

    public commentsModel(String commentText, long commentedAt, String commentedBy) {
        this.commentText = commentText;
        this.commentedAt = commentedAt;
        this.commentedBy = commentedBy;
    }

    public commentsModel() {
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public long getCommentedAt() {
        return commentedAt;
    }

    public void setCommentedAt(long commentedAt) {
        this.commentedAt = commentedAt;
    }

    public String getCommentedBy() {
        return commentedBy;
    }

    public void setCommentedBy(String commentedBy) {
        this.commentedBy = commentedBy;
    }
}
