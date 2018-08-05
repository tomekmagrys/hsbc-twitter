package com.hsbc.interview.homework.service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FollowServiceConcurrentTest {

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

    @Before
    public void setUp() throws Exception {
        userService.createIfNotExists(TestConstants.JOHN_SNOW);
        userService.createIfNotExists(TestConstants.KHALEESI);
    }

    @Test
    public void concurrentFollowTest() throws InterruptedException {

        ExecutorService pool = Executors.newFixedThreadPool(100);

        CountDownLatch countDownLatch = new CountDownLatch(100);

        for (int i = 0; i < 100; i++) {
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    followService.follow(TestConstants.JOHN_SNOW, TestConstants.KHALEESI);
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        Assert.assertEquals(1L, followService.findAll().spliterator().getExactSizeIfKnown());
    }

}