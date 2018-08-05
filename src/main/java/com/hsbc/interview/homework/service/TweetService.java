package com.hsbc.interview.homework.service;


import com.hsbc.interview.homework.common.Constants;
import com.hsbc.interview.homework.db.Tweet;
import com.hsbc.interview.homework.db.User;
import com.hsbc.interview.homework.repository.TweetRepository;
import com.hsbc.interview.homework.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class TweetService {

    @Autowired
    TweetRepository tweetRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    public Tweet tweet(String login, String post) throws Exception {
        if(StringUtils.isEmpty(post)){
            throw new IllegalArgumentException(Constants.CANNOT_POST_EMPTY_TWEET);
        }
        if(StringUtils.isEmpty(login)){
            throw new IllegalArgumentException(Constants.CANNOT_POST_ANONYMOUSLY);
        }
        if(post.length() > 140){
            throw new IllegalArgumentException(Constants.SORRY_TWEET_IS_LIMITED_TO_140_CHARACTERS);
        }

        User user = userService.createIfNotExists(login);

        Tweet tweet = new Tweet();

        tweet.setAuthor(user);
        tweet.setPost(post);

        tweet.setDateTime(ZonedDateTime.now(ZoneId.of("UTC")));

        tweetRepository.save(tweet);

        return tweet;

    }

    public List<Tweet> getWall(String login){
        User user = userRepository.findByLogin(login);

        if(user == null) {
            throw new IllegalArgumentException("User not found");
        }

        return tweetRepository.findAllByAuthorOrderByDateTimeDesc(user);
    }

    public List<Tweet> getTimeline(String login){
        User user = userRepository.findByLogin(login);

        if(user == null) {
            throw new IllegalArgumentException("User not found");
        }

        return tweetRepository.getTimeline(user);
    }


}
