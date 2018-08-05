package com.hsbc.interview.homework.service;

import com.hsbc.interview.homework.request.FollowRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FollowServiceWebApiIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    TweetService tweetService;

    @Autowired
    FollowService followService;

    @Autowired
    UserService userService;

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

    @Test
    public void followYourselfTest() {
        String response = follow(TestConstants.JOHN_SNOW, TestConstants.JOHN_SNOW);
        assertFalse(StringUtils.isEmpty(response));
    }

    @Test
    public void followingNotExistingUserTest() {
        String response = follow(TestConstants.JOHN_SNOW, TestConstants.ARYA_STARK);
        assertFalse(StringUtils.isEmpty(response));
    }

    @Test
    public void followedNotExistingUserTest() {
        String response = follow(TestConstants.ARYA_STARK, TestConstants.JOHN_SNOW);
        assertFalse(StringUtils.isEmpty(response));
    }

    @Test
    public void followingEachOtherTest() {
        String response = follow(TestConstants.JOHN_SNOW, TestConstants.KHALEESI);
        assertTrue(StringUtils.isEmpty(response));
        response = follow(TestConstants.KHALEESI, TestConstants.JOHN_SNOW);
        assertTrue(StringUtils.isEmpty(response));
    }

    @Test
    public void followingTwiceTest() {
        String response = follow(TestConstants.JOHN_SNOW, TestConstants.KHALEESI);
        assertTrue(StringUtils.isEmpty(response));
        response = follow(TestConstants.JOHN_SNOW, TestConstants.KHALEESI);
        assertTrue(StringUtils.isEmpty(response));
    }

    private String follow(String following, String followed) {
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/api/follow", new FollowRequest(following, followed), String.class);
        return responseEntity.getBody();

    }


}