package com.hsbc.interview.homework.service;

import com.hsbc.interview.homework.request.FollowRequest;
import com.hsbc.interview.homework.request.TweetRequest;
import com.hsbc.interview.homework.response.TweetListResponse;
import com.hsbc.interview.homework.response.TweetResponse;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TweetServiceWebApiIntegrationTest {

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

    @After
    public void tearDown(){
        cleanUpService.cleanUp();
    }

    @Test
    public void singleTweetTest() {
        tweet(TestConstants.JOHN_SNOW, TestConstants.I_NEED_YOU_DRAGONS_KHALEESI);
    }

    @Test
    public void twoTweetsOfTheSameUserTest() {
        tweet(TestConstants.JOHN_SNOW, TestConstants.I_NEED_YOU_DRAGONS_KHALEESI);
        tweet(TestConstants.JOHN_SNOW, TestConstants.IT_SEEMS_THAT_NIGHT_KING_IS_REALLY_POWERFUL);
    }

    @Test
    public void tweetsOfTheSameUserCheckThatWallIsReverseChronologicalTest() {
        tweet(TestConstants.JOHN_SNOW, TestConstants.I_NEED_YOU_DRAGONS_KHALEESI);
        tweet(TestConstants.JOHN_SNOW, TestConstants.IT_SEEMS_THAT_NIGHT_KING_IS_REALLY_POWERFUL);
        tweet(TestConstants.JOHN_SNOW, TestConstants.HELP_FROM_LORD_BAYLISH_LITTLEFINGER_MAY_BE_CRUCIAL);

        ResponseEntity<TweetListResponse> responseEntity =
                restTemplate.postForEntity("/api/wall", TestConstants.JOHN_SNOW, TweetListResponse.class);
        TweetListResponse wallResponse = responseEntity.getBody();

        List<TweetResponse> tweetResponses = wallResponse.getTweets();

        assertEquals(3, tweetResponses.size());

        TweetResponse firstTweet = tweetResponses.get(0);
        TweetResponse secondTweet = tweetResponses.get(1);
        TweetResponse thirdTweet = tweetResponses.get(2);

        ZonedDateTime firstDateTime = firstTweet.getDateTime();
        ZonedDateTime secondDateTime = secondTweet.getDateTime();
        ZonedDateTime thirdDateTime = thirdTweet.getDateTime();

        assertTrue(firstDateTime.isAfter(secondDateTime));
        assertTrue(secondDateTime.isAfter(thirdDateTime));

    }

    @Test
    public void followTest() {
        tweet(TestConstants.JOHN_SNOW, TestConstants.I_NEED_YOU_DRAGONS_KHALEESI);
        tweet(TestConstants.KHALEESI, TestConstants.I_M_COMING);
        tweet(TestConstants.JOHN_SNOW, TestConstants.IT_SEEMS_THAT_NIGHT_KING_IS_REALLY_POWERFUL);
        tweet(TestConstants.ARYA_STARK, TestConstants.NOBODY_IS_GOING_TO_FOLLOW_ME);

        follow(TestConstants.JOHN_SNOW, TestConstants.KHALEESI);
        follow(TestConstants.KHALEESI, TestConstants.JOHN_SNOW);
        follow(TestConstants.ARYA_STARK, TestConstants.KHALEESI);
        follow(TestConstants.ARYA_STARK, TestConstants.JOHN_SNOW);


        ResponseEntity<TweetListResponse> responseEntity =
                restTemplate.postForEntity("/api/timeline", TestConstants.JOHN_SNOW, TweetListResponse.class);
        TweetListResponse wallResponse = responseEntity.getBody();

        List<TweetResponse> tweetResponses = wallResponse.getTweets();
        assertEquals(1, tweetResponses.size());
        assertEquals(TestConstants.I_M_COMING, tweetResponses.get(0).getPost());

        responseEntity =
                restTemplate.postForEntity("/api/timeline", TestConstants.KHALEESI, TweetListResponse.class);
        wallResponse = responseEntity.getBody();
        tweetResponses = wallResponse.getTweets();
        assertEquals(2, tweetResponses.size());

        assertEquals(TestConstants.IT_SEEMS_THAT_NIGHT_KING_IS_REALLY_POWERFUL, tweetResponses.get(0).getPost());
        assertEquals(TestConstants.I_NEED_YOU_DRAGONS_KHALEESI, tweetResponses.get(1).getPost());

        responseEntity =
                restTemplate.postForEntity("/api/timeline", TestConstants.ARYA_STARK, TweetListResponse.class);

        wallResponse = responseEntity.getBody();
        tweetResponses = wallResponse.getTweets();
        assertEquals(3, tweetResponses.size());

        assertEquals(TestConstants.IT_SEEMS_THAT_NIGHT_KING_IS_REALLY_POWERFUL, tweetResponses.get(0).getPost());
        assertEquals(TestConstants.I_M_COMING, tweetResponses.get(1).getPost());
        assertEquals(TestConstants.I_NEED_YOU_DRAGONS_KHALEESI, tweetResponses.get(2).getPost());

    }

    @Test
    public void getWallOfNotExistingUserTest() {
        ResponseEntity<TweetListResponse> responseEntity =
                restTemplate.postForEntity("/api/wall", TestConstants.KHALEESI, TweetListResponse.class);

        TweetListResponse wallResponse = responseEntity.getBody();

        assertNotNull(wallResponse.getErrorMessage());

    }

    @Test
    public void emptyTweetTest() {
        ResponseEntity<TweetResponse> responseEntity =
                restTemplate.postForEntity("/api/tweet", new TweetRequest(TestConstants.JOHN_SNOW, ""), TweetResponse.class);
        TweetResponse tweetResponse = responseEntity.getBody();
        assertNotNull(tweetResponse.getErrorMessage());
    }

    @Test
    public void testTweetLongerThan140Letters() {
        ResponseEntity<TweetResponse> responseEntity =
                restTemplate.postForEntity("/api/tweet", new TweetRequest(TestConstants.JOHN_SNOW,  TestConstants.I_NEED_YOU_DRAGONS_KHALEESI_140), TweetResponse.class);
        TweetResponse tweetResponse = responseEntity.getBody();
        assertNotNull(tweetResponse.getErrorMessage());
    }

    private void tweet(String login, String post) {
        ResponseEntity<TweetResponse> responseEntity =
                restTemplate.postForEntity("/api/tweet", new TweetRequest(login, post), TweetResponse.class);
        TweetResponse tweetResponse = responseEntity.getBody();

        assertEquals(login, tweetResponse.getLogin());
        assertEquals(post, tweetResponse.getPost());
    }

    private void follow(String following, String followed) {
        restTemplate.postForEntity("/api/follow", new FollowRequest(following, followed), String.class);

    }


}