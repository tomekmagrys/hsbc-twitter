package com.hsbc.interview.homework.service;

import com.hsbc.interview.homework.db.User;
import com.hsbc.interview.homework.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
public class UserService {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    private ExecutorService executorService = Executors.newFixedThreadPool(1);

    public User createIfNotExists(String login) throws Exception {

        Future<User> userFuture = executorService.submit(new Callable<User>() {
            @Override
            public User call() {
                User user = userRepository.findByLogin(login);
                if (user != null) {
                    return user;
                }
                user = new User();
                user.setLogin(login);
                userRepository.save(user);
                return user;
            }
        });

        try {
            return userFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error(e.getMessage(), e);
            throw new Exception("Error during user creation");
        }
    }

    public Iterable<User> findAll(){
        return  userRepository.findAll();
    }

}
