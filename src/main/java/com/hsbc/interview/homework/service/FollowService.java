package com.hsbc.interview.homework.service;

import com.hsbc.interview.homework.common.Constants;
import com.hsbc.interview.homework.db.Follow;
import com.hsbc.interview.homework.db.User;
import com.hsbc.interview.homework.repository.FollowRepository;
import com.hsbc.interview.homework.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.*;

@Service
public class FollowService {

    private final static Logger LOGGER = LoggerFactory.getLogger(FollowService.class);

    @Autowired
    FollowRepository followRepository;

    @Autowired
    UserRepository userRepository;

    private ExecutorService executorService = Executors.newFixedThreadPool(1);

    public void follow(String following, String followed) {
        if(StringUtils.isEmpty(followed) || StringUtils.isEmpty(following)){
            throw new IllegalArgumentException(Constants.BOTH_FOLLOWING_AND_FOLLOWED_HAVE_TO_BE_SPECIFIED);
        }
        if (following.equals(followed)) {
            throw new IllegalArgumentException(Constants.YOU_CANNOT_FOLLOW_YOURSELF_SORRY_ABOUT_THAT);
        }

        User followingUser = userRepository.findByLogin(following);

        if (followingUser == null) {
            throw new IllegalArgumentException(Constants.USER_NOT_FOUND);
        }

        User followedUser = userRepository.findByLogin(followed);

        if (followedUser == null) {
            throw new IllegalArgumentException(Constants.USER_NOT_FOUND);
        }

        Future<Follow> futureFollow = executorService.submit(new Callable<Follow>() {
            @Override
            public Follow call() {
                Follow follow = followRepository.findByFollowingAndFollowed(followingUser, followedUser);
                if (follow != null) {
                    return follow;
                }
                follow = new Follow();
                follow.setFollowed(followedUser);
                follow.setFollowing(followingUser);
                followRepository.save(follow);
                return follow;
            }
        });

        try {
            futureFollow.get();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error(e.getMessage(), e);
        }

    }

    public Iterable<Follow> findAll() {
        return followRepository.findAll();
    }
}
