package com.hsbc.interview.homework.repository;

import com.hsbc.interview.homework.db.Tweet;
import com.hsbc.interview.homework.db.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TweetRepository extends CrudRepository<Tweet, Long> {

    List<Tweet> findAllByAuthorOrderByDateTimeDesc(User author);

    @Query("SELECT t FROM Tweet t JOIN Follow f ON t.author = f.followed WHERE f.following = ?1 ORDER BY t.dateTime DESC")
    List<Tweet> getTimeline(User login);



}