package com.hsbc.interview.homework.service;

import com.hsbc.interview.homework.db.Tweet;
import com.hsbc.interview.homework.db.User;
import com.hsbc.interview.homework.repository.TweetRepository;
import com.hsbc.interview.homework.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TweetServiceTest {

    @Mock
    TweetRepository tweetRepository;

    @Mock
    UserService userService;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    TweetService tweetService;

    @Before
    public void setUp() throws Exception {
        User johnSnow = new User();
        johnSnow.setLogin(TestConstants.JOHN_SNOW);

        when(userService.createIfNotExists(TestConstants.JOHN_SNOW)).thenReturn(johnSnow);
        when(userRepository.findByLogin(TestConstants.JOHN_SNOW)).thenReturn(johnSnow);

    }

    @Test
    public void singleTweetTest() throws Exception {
        tweetService.tweet(TestConstants.JOHN_SNOW, TestConstants.I_NEED_YOU_DRAGONS_KHALEESI);

        ArgumentCaptor<Tweet> tweetArgumentCaptor = ArgumentCaptor.forClass(Tweet.class);

        verify(tweetRepository).save(tweetArgumentCaptor.capture());

        assertEquals(TestConstants.I_NEED_YOU_DRAGONS_KHALEESI, tweetArgumentCaptor.getValue().getPost());
        assertEquals(TestConstants.JOHN_SNOW, tweetArgumentCaptor.getValue().getAuthor().getLogin());


    }

    @Test
    public void getWallTest() {
        tweetService.getWall(TestConstants.JOHN_SNOW);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(tweetRepository).findAllByAuthorOrderByDateTimeDesc(userArgumentCaptor.capture());

        assertEquals(TestConstants.JOHN_SNOW, userArgumentCaptor.getValue().getLogin());

    }


    @Test
    public void getTimelineTest() {
        tweetService.getTimeline(TestConstants.JOHN_SNOW);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(tweetRepository).getTimeline(userArgumentCaptor.capture());

        assertEquals(TestConstants.JOHN_SNOW, userArgumentCaptor.getValue().getLogin());

    }


    @Test(expected = IllegalArgumentException.class)
    public void emptyTweetTest() throws Exception {
        tweetService.tweet(TestConstants.JOHN_SNOW, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullTweetTest() throws Exception {
        tweetService.tweet(TestConstants.JOHN_SNOW, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullUserTest() throws Exception {
        tweetService.tweet(null, TestConstants.I_NEED_YOU_DRAGONS_KHALEESI);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTweetLongerThan140Letters() throws Exception {
        tweetService.tweet(TestConstants.JOHN_SNOW, TestConstants.I_NEED_YOU_DRAGONS_KHALEESI_140);
    }


    @Test(expected = IllegalArgumentException.class)
    public void getWallOfNotExistingUserTest() {
        tweetService.getWall(TestConstants.ARYA_STARK);
    }


    @Test(expected = IllegalArgumentException.class)
    public void getWallOfNullUserTest() {
        tweetService.getWall(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getTimelineOfNotExistingUserTest() {
        tweetService.getTimeline(TestConstants.ARYA_STARK);
    }


    @Test(expected = IllegalArgumentException.class)
    public void getTimelineOfNullUserTest() {
        tweetService.getTimeline(null);
    }


}