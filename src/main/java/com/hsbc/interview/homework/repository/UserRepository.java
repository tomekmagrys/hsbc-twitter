package com.hsbc.interview.homework.repository;

import com.hsbc.interview.homework.db.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByLogin(String login);
}