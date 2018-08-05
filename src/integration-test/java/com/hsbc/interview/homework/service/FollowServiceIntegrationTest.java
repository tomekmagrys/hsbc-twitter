package com.hsbc.interview.homework.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FollowServiceIntegrationTest {

    @Autowired
    UserService userService;

    @Autowired
    FollowService followService;

    @Autowired
    CleanUpService cleanUpService;

    @Before
    public void setUp() throws Exception {
        userService.createIfNotExists(TestConstants.JOHN_SNOW);
        userService.createIfNotExists(TestConstants.KHALEESI);
    }

    @After
    public void tearDown() {
        cleanUpService.cleanUp();
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void followYourselfTest() {
        followService.follow(TestConstants.JOHN_SNOW, TestConstants.JOHN_SNOW);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void followingNotExistingUserTest() {
        followService.follow(TestConstants.ARYA_STARK, TestConstants.JOHN_SNOW);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void followedNotExistingUserTest() {
        followService.follow(TestConstants.JOHN_SNOW, TestConstants.ARYA_STARK);
    }

    @Test
    public void followingTest() {
        followService.follow(TestConstants.JOHN_SNOW, TestConstants.KHALEESI);
        followService.follow(TestConstants.KHALEESI, TestConstants.JOHN_SNOW);
    }

    @Test
    public void followingTwiceTest() {
        followService.follow(TestConstants.JOHN_SNOW, TestConstants.KHALEESI);
        followService.follow(TestConstants.JOHN_SNOW, TestConstants.KHALEESI);
    }

}