package com.hsbc.interview.homework.service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceConcurrentTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(TwitterServiceConcurrentTest.class);

    @Autowired
    UserService userService;

    @Autowired
    CleanUpService cleanUpService;

    @After
    public void tearDown(){
        cleanUpService.cleanUp();
    }

    @Test
    public void concurrentUserAddTest() throws InterruptedException {

        ExecutorService pool = Executors.newFixedThreadPool(100);

        CountDownLatch countDownLatch = new CountDownLatch(100);

        for (int i = 0; i < 100; i++) {
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        userService.createIfNotExists(TestConstants.JOHN_SNOW);
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage(), e);
                    } finally {
                        countDownLatch.countDown();
                    }
                }
            });
        }

        countDownLatch.await();
        Assert.assertEquals(1L, userService.findAll().spliterator().getExactSizeIfKnown());
    }

}