package com.hsbc.interview.homework.request;

public class TweetRequest {

    private String login;
    private String post;

    public TweetRequest() {
    }

    public TweetRequest(String login, String post) {
        this.login = login;
        this.post = post;
    }

    public String getLogin() {
        return login;
    }

    public String getPost() {
        return post;
    }
}
