package com.hsbc.interview.homework.service;

import com.hsbc.interview.homework.db.Follow;
import com.hsbc.interview.homework.db.User;
import com.hsbc.interview.homework.repository.FollowRepository;
import com.hsbc.interview.homework.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class FollowServiceTest {

    @Mock
    UserRepository userService;

    @Mock
    FollowRepository followRepository;

    @InjectMocks
    FollowService followService;


    @Before
    public void setUp() {
        User johnSnow = new User();
        johnSnow.setLogin(TestConstants.JOHN_SNOW);

        when(userService.findByLogin(TestConstants.JOHN_SNOW)).thenReturn(johnSnow);

        User khaleesi = new User();
        khaleesi.setLogin(TestConstants.KHALEESI);

        when(userService.findByLogin(TestConstants.KHALEESI)).thenReturn(khaleesi);

    }


    @Test(expected = IllegalArgumentException.class)
    public void followYourselfTest() {
        followService.follow(TestConstants.JOHN_SNOW, TestConstants.JOHN_SNOW);
    }

    @Test(expected = IllegalArgumentException.class)
    public void followingNotExistingUserTest() {
        followService.follow(TestConstants.ARYA_STARK, TestConstants.JOHN_SNOW);
    }

    @Test(expected = IllegalArgumentException.class)
    public void followedNotExistingUserTest() {
        followService.follow(TestConstants.JOHN_SNOW, TestConstants.ARYA_STARK);
    }

    @Test
    public void followingTest() {
        followService.follow(TestConstants.JOHN_SNOW, TestConstants.KHALEESI);
        followService.follow(TestConstants.KHALEESI, TestConstants.JOHN_SNOW);

        ArgumentCaptor<Follow> followArgumentCaptor = ArgumentCaptor.forClass(Follow.class);

        verify(followRepository,times(2)).save(followArgumentCaptor.capture());

        assertEquals(2, followArgumentCaptor.getAllValues().size());

        assertEquals(TestConstants.KHALEESI , followArgumentCaptor.getAllValues().get(0).getFollowed().getLogin());
        assertEquals(TestConstants.JOHN_SNOW , followArgumentCaptor.getAllValues().get(0).getFollowing().getLogin());
        assertEquals(TestConstants.JOHN_SNOW , followArgumentCaptor.getAllValues().get(1).getFollowed().getLogin());
        assertEquals(TestConstants.KHALEESI , followArgumentCaptor.getAllValues().get(1).getFollowing().getLogin());

    }

    @Test
    public void followingTwiceTest() {
        followService.follow(TestConstants.JOHN_SNOW, TestConstants.KHALEESI);
        followService.follow(TestConstants.JOHN_SNOW, TestConstants.KHALEESI);

        ArgumentCaptor<Follow> followArgumentCaptor = ArgumentCaptor.forClass(Follow.class);

        verify(followRepository,times(2)).save(followArgumentCaptor.capture());

        assertEquals(2, followArgumentCaptor.getAllValues().size());

        assertEquals(TestConstants.KHALEESI , followArgumentCaptor.getAllValues().get(0).getFollowed().getLogin());
        assertEquals(TestConstants.JOHN_SNOW , followArgumentCaptor.getAllValues().get(0).getFollowing().getLogin());
        assertEquals(TestConstants.KHALEESI , followArgumentCaptor.getAllValues().get(1).getFollowed().getLogin());
        assertEquals(TestConstants.JOHN_SNOW , followArgumentCaptor.getAllValues().get(1).getFollowing().getLogin());
    }

}