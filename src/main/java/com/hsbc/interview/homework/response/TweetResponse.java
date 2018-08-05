package com.hsbc.interview.homework.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hsbc.interview.homework.db.Tweet;

import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TweetResponse {

    private String errorMessage;
    private String login;
    private String post;
    private ZonedDateTime dateTime;

    public TweetResponse() {
    }

    public static TweetResponse error(String errorMessage) {
        TweetResponse tweetResponse = new TweetResponse();
        tweetResponse.setErrorMessage(errorMessage);
        return tweetResponse;
    }

    public static TweetResponse ok(Tweet tweet) {
        TweetResponse tweetResponse = new TweetResponse();

        tweetResponse.setLogin(tweet.getAuthor().getLogin());
        tweetResponse.setPost(tweet.getPost());
        tweetResponse.setDateTime(tweet.getDateTime());

        return tweetResponse;
    }

    public String getLogin() {
        return login;
    }

    public String getPost() {
        return post;
    }

    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
