package com.davita;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.gitana.platform.client.Gitana;
import org.gitana.platform.client.branch.Branch;
import org.gitana.platform.client.node.Node;
import org.gitana.platform.client.platform.Platform;
import org.gitana.platform.client.repository.Repository;
import org.gitana.platform.support.Pagination;
import org.gitana.platform.support.QueryBuilder;
import org.gitana.platform.support.ResultMap;
import org.gitana.platform.support.Sorting;
import org.gitana.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kmasood on 7/1/16.
 */
@RestController
@RequestMapping("users")
public class UserController {

    private static final String PARAM_USER_FNAME = "fname";
    private static final String PARAM_USER_LNAME= "lname";

    private static final String USER_ID_VALIDATION_ERROR = "Invalid userId provided: ";
    private static final String USER_ID_DUP_ERROR = "UserId already provisioned in system: ";
    private static final String CONTENT_ID_VALIDATION_ERROR = "Invalid contentId provided: ";

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("")
    public List<User> getUsers() {

        List<User> userList = new ArrayList<User>();

        Iterable<User> users = userRepository.findAll();

        for (User user: users) {
            userList.add(user);
        }

        return userList;
    }


    @RequestMapping("{userId}")
    public User getUser(@PathVariable String userId) {

        User user = userRepository.findByUid(userId);

        if (user == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, USER_ID_VALIDATION_ERROR + userId);
        }

        return user;
    }


    @RequestMapping("{userId}/readArticles")
    public List<String> readArticles(@PathVariable String userId) {

        List<String> readArticles;

        User user = userRepository.findByUid(userId);

        if (user != null) {
            readArticles = user.getReadArticles();
        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, USER_ID_VALIDATION_ERROR + userId);
        }

        return readArticles;
    }


    @RequestMapping(value = "{userId}/readArticles/{contentId}", method = RequestMethod.POST)
    public void readArticles(@PathVariable String userId, @PathVariable String contentId) {

        Content content = contentRepository.findOne(contentId);

        if (content != null) {

            User user = userRepository.findByUid(userId);

            if (user != null) {
                user.addReadContent(content.getId());
                userRepository.save(user);
            } else {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, USER_ID_VALIDATION_ERROR + userId);
            }
        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, CONTENT_ID_VALIDATION_ERROR + contentId);
        }
    }


    @RequestMapping("{userId}/likedArticles")
    public List<String> likedArticles(@PathVariable String userId) {

        StringBuilder response = new StringBuilder();
        List<String> likedArticles = new ArrayList<String>();

        User user = userRepository.findByUid(userId);

        if (user != null) {
//            List<Content> likedArticles = user.getLikedArticles();
            likedArticles = user.getLikedArticles();

//            for (Content content : likedArticles) {
//                response.append(content.toString());
//            }
        }

//        return (userId + " liked the following articles: " + response.toString());
        return likedArticles;
    }


    @RequestMapping(value = "{userId}/likedArticles/{contentId}", method = RequestMethod.POST)
    public void likeArticles(@PathVariable String userId, @PathVariable String contentId) {

        Content content = contentRepository.findOne(contentId);

        if (content != null) {

            User user = userRepository.findByUid(userId);

            if (user != null) {
                user.addlikedContent(content.getId());
                userRepository.save(user);
            } else {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, USER_ID_VALIDATION_ERROR + userId);
            }
        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, CONTENT_ID_VALIDATION_ERROR + contentId);
        }
    }


    @RequestMapping(value = "{userId}", method = RequestMethod.POST)
    public void createUser(@PathVariable String userId,
                           @RequestParam(value=PARAM_USER_FNAME) String fname,
                           @RequestParam(value=PARAM_USER_LNAME) String lname) {

        User user = userRepository.findByUid(userId);

        if (user != null) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, USER_ID_DUP_ERROR + userId);
        } else {
            userRepository.save(new User(userId, fname + " " + lname));
        }
    }


    @RequestMapping(value = "{userId}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable String userId) {

        User user = userRepository.findByUid(userId);

        if (user != null) {
            userRepository.delete(user);
        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, USER_ID_VALIDATION_ERROR + userId);
        }
    }

}
