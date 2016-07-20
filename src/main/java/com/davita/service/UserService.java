package com.davita.service;

import com.davita.exception.ApplicationException;
import com.davita.exception.NotFoundException;
import com.davita.exception.ValidationException;
import com.davita.model.User;

import java.net.URI;
import java.util.List;
import java.util.Optional;

/**
 * Created by kmasood on 7/20/16.
 */
public interface UserService {
    List<User> getAllUser();
    User getUser(String userId) throws NotFoundException;
    List<String> getReadArticles(String userId) throws NotFoundException;
    void addReadArticle(String userId, String contentId) throws NotFoundException, ApplicationException;
    List<String> getLikedArticles(String userId) throws NotFoundException;
    void addLikedArticle(String userId, String contentId) throws NotFoundException, ApplicationException;
    URI createUser(User user) throws ValidationException ;
    void deleteUser(String userId) throws ValidationException ;
}
