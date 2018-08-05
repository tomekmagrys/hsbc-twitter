package com.hsbc.interview.homework.controller;

import com.hsbc.interview.homework.db.Tweet;
import com.hsbc.interview.homework.request.FollowRequest;
import com.hsbc.interview.homework.request.TweetRequest;
import com.hsbc.interview.homework.response.TweetListResponse;
import com.hsbc.interview.homework.response.TweetResponse;
import com.hsbc.interview.homework.service.FollowService;
import com.hsbc.interview.homework.service.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final TweetService tweetService;

    private final FollowService followService;

    @Autowired
    public ApiController(TweetService tweetService, FollowService followService) {
        this.tweetService = tweetService;
        this.followService = followService;
    }

    @RequestMapping(value = "/tweet", method = RequestMethod.POST)
    public TweetResponse tweet(@RequestBody TweetRequest tweetRequest) {
        try {
            Tweet tweet = tweetService.tweet(tweetRequest.getLogin(), tweetRequest.getPost());

            return TweetResponse.ok(tweet);
        } catch (Exception e) {
            return TweetResponse.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/wall", method = RequestMethod.POST)
    public TweetListResponse wall(@RequestBody String login) {
        try {
            List<Tweet> tweets = tweetService.getWall(login);

            return convertTweetsToTweetResponse(tweets);

        } catch (Exception e) {
            return TweetListResponse.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/follow", method = RequestMethod.POST)
    public String follow(@RequestBody FollowRequest followRequest) {
        try {
            followService.follow(followRequest.getFollowing(), followRequest.getFollowed());
            return "";

        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/timeline", method = RequestMethod.POST)
    public TweetListResponse timeline(@RequestBody String login) {
        try {
            List<Tweet> tweets = tweetService.getTimeline(login);

            return convertTweetsToTweetResponse(tweets);

        } catch (Exception e) {
            return TweetListResponse.error(e.getMessage());
        }
    }

    private TweetListResponse convertTweetsToTweetResponse(List<Tweet> tweets) {
        return TweetListResponse.ok(
                tweets.stream().map(TweetResponse::ok).collect(Collectors.toList()));
    }
}