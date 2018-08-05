package com.hsbc.interview.homework.repository;

import com.hsbc.interview.homework.db.Follow;
import com.hsbc.interview.homework.db.User;
import org.springframework.data.repository.CrudRepository;

public interface FollowRepository extends CrudRepository<Follow, Long> {

    Follow findByFollowingAndFollowed(User following, User followed);

}