package com.hsbc.interview.homework.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TweetListResponse {

    private String errorMessage;

    private List<TweetResponse> tweets;

    public TweetListResponse(){

    }

    private TweetListResponse(String status, List<TweetResponse> tweets){
        this.errorMessage = status;
        this.tweets = tweets;
    }

    public static TweetListResponse error(String error){
        return new TweetListResponse(error, null);
    }

    public static TweetListResponse ok(List<TweetResponse> tweets){
        return new TweetListResponse(null, tweets);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<TweetResponse> getTweets() {
        return tweets;
    }

    public void setTweets(List<TweetResponse> tweets) {
        this.tweets = tweets;
    }
}
