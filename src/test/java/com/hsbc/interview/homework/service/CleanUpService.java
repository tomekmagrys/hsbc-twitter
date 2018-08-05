package com.hsbc.interview.homework.service;

import com.hsbc.interview.homework.repository.FollowRepository;
import com.hsbc.interview.homework.repository.TweetRepository;
import com.hsbc.interview.homework.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CleanUpService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    FollowRepository followRepository;

    @Autowired
    TweetRepository tweetRepository;

    public void cleanUp(){
        followRepository.deleteAll();
        tweetRepository.deleteAll();
        userRepository.deleteAll();
    }

}
