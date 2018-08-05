package com.hsbc.interview.homework.service;

import com.hsbc.interview.homework.db.Tweet;
import com.hsbc.interview.homework.db.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TweetServiceIntegrationTest {

    @Autowired
    TweetService tweetService;

    @Autowired
    FollowService followService;

    @Autowired
    UserService userService;

    @Autowired
    CleanUpService cleanUpService;

    @After
    public void tearDown() {
        cleanUpService.cleanUp();
    }

    @Test
    public void singleTweetTest() throws Exception {
        tweetService.tweet(TestConstants.JOHN_SNOW, TestConstants.I_NEED_YOU_DRAGONS_KHALEESI);
    }

    @Test
    public void twoTweetsOfTheSameUserCheckThatAccountIsCreatedOnceTest() throws Exception {
        tweetService.tweet(TestConstants.JOHN_SNOW, TestConstants.I_NEED_YOU_DRAGONS_KHALEESI);
        tweetService.tweet(TestConstants.JOHN_SNOW, TestConstants.IT_SEEMS_THAT_NIGHT_KING_IS_REALLY_POWERFUL);

        Iterable<User> users = userService.findAll();
        Iterator<User> userIterator = users.iterator();
        assertTrue(userIterator.hasNext());

        User johnSnow = userIterator.next();

        Assert.assertEquals(johnSnow.getLogin(), TestConstants.JOHN_SNOW);
        assertFalse(userIterator.hasNext());
    }

    @Test
    public void twoTweetsOfTheSameUserCheckThatWallIsReverseChronologicalTest() throws Exception {
        tweetService.tweet(TestConstants.JOHN_SNOW, TestConstants.I_NEED_YOU_DRAGONS_KHALEESI);
        tweetService.tweet(TestConstants.JOHN_SNOW, TestConstants.IT_SEEMS_THAT_NIGHT_KING_IS_REALLY_POWERFUL);
        tweetService.tweet(TestConstants.JOHN_SNOW, TestConstants.HELP_FROM_LORD_BAYLISH_LITTLEFINGER_MAY_BE_CRUCIAL);

        Iterator<Tweet> johnSnowTweetsIterator = tweetService.getWall(TestConstants.JOHN_SNOW).iterator();

        Tweet first = johnSnowTweetsIterator.next();
        Tweet second = johnSnowTweetsIterator.next();
        Tweet third = johnSnowTweetsIterator.next();

        assertTrue(first.getDateTime().isAfter(second.getDateTime()));
        assertTrue(second.getDateTime().isAfter(third.getDateTime()));

    }

    @Test
    public void followTest() throws Exception {
        tweetService.tweet(TestConstants.JOHN_SNOW, TestConstants.I_NEED_YOU_DRAGONS_KHALEESI);
        tweetService.tweet(TestConstants.KHALEESI, TestConstants.I_M_COMING);
        tweetService.tweet(TestConstants.JOHN_SNOW, TestConstants.IT_SEEMS_THAT_NIGHT_KING_IS_REALLY_POWERFUL);
        tweetService.tweet(TestConstants.ARYA_STARK, TestConstants.NOBODY_IS_GOING_TO_FOLLOW_ME);

        followService.follow(TestConstants.JOHN_SNOW, TestConstants.KHALEESI);
        followService.follow(TestConstants.KHALEESI, TestConstants.JOHN_SNOW);
        followService.follow(TestConstants.ARYA_STARK, TestConstants.KHALEESI);
        followService.follow(TestConstants.ARYA_STARK, TestConstants.JOHN_SNOW);


        Iterator<Tweet> johnSnowTimelineIterator = tweetService.getTimeline(TestConstants.JOHN_SNOW).iterator();

        Tweet first = johnSnowTimelineIterator.next();

        assertEquals(TestConstants.I_M_COMING, first.getPost());
        assertFalse(johnSnowTimelineIterator.hasNext());


        Iterator<Tweet> khaleesiTimelineIterator = tweetService.getTimeline(TestConstants.KHALEESI).iterator();

        first = khaleesiTimelineIterator.next();
        Tweet second = khaleesiTimelineIterator.next();

        assertEquals(TestConstants.IT_SEEMS_THAT_NIGHT_KING_IS_REALLY_POWERFUL, first.getPost());
        assertEquals(TestConstants.I_NEED_YOU_DRAGONS_KHALEESI, second.getPost());

        assertTrue(first.getDateTime().isAfter(second.getDateTime()));
        assertFalse(khaleesiTimelineIterator.hasNext());

        Iterator<Tweet> aryaStarkTimelineIterator = tweetService.getTimeline(TestConstants.ARYA_STARK).iterator();

        first = aryaStarkTimelineIterator.next();
        second = aryaStarkTimelineIterator.next();
        Tweet third = aryaStarkTimelineIterator.next();

        assertTrue(first.getDateTime().isAfter(second.getDateTime()));
        assertTrue(second.getDateTime().isAfter(third.getDateTime()));

        assertEquals(TestConstants.IT_SEEMS_THAT_NIGHT_KING_IS_REALLY_POWERFUL, first.getPost());
        assertEquals(TestConstants.I_M_COMING, second.getPost());
        assertEquals(TestConstants.I_NEED_YOU_DRAGONS_KHALEESI, third.getPost());


        assertFalse(aryaStarkTimelineIterator.hasNext());

    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void getWallOfNotExistingUserTest() throws Exception {
        tweetService.tweet(TestConstants.JOHN_SNOW, TestConstants.I_NEED_YOU_DRAGONS_KHALEESI);

        tweetService.getWall(TestConstants.KHALEESI);

    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void emptyTweetTest() throws Exception {
        tweetService.tweet(TestConstants.JOHN_SNOW, "");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testTweetLongerThan140Letters() throws Exception {
        tweetService.tweet(TestConstants.JOHN_SNOW, TestConstants.I_NEED_YOU_DRAGONS_KHALEESI_140);
    }


}