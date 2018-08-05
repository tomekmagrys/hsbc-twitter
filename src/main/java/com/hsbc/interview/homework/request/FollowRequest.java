package com.hsbc.interview.homework.request;

public class FollowRequest {

    private String following;
    private String followed;

    public FollowRequest() {
    }

    public FollowRequest(String following, String followed) {
        this.following = following;
        this.followed = followed;
    }

    public String getFollowing() {
        return following;
    }

    public String getFollowed() {
        return followed;
    }
}
