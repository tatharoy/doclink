package com.davita.service;

import com.davita.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Created by kmasood on 7/20/16.
 */
public interface UserService {
    ResponseEntity<List<User>> getUsers();
    ResponseEntity<User> getUser(String userId);
    ResponseEntity readArticles(String userId, String contentId);
    ResponseEntity<List<String>> likedArticles(String userId);
    ResponseEntity likeArticles(String userId, String contentId);
    ResponseEntity createUser(User user);
    ResponseEntity deleteUser(String userId);
}
