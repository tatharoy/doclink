package com.davita.controller;

import com.davita.exception.ApplicationException;
import com.davita.exception.NotFoundException;
import com.davita.exception.ValidationException;
import com.davita.model.User;
import com.davita.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

/**
 * Created by kmasood on 7/1/16.
 */
@RestController
@RequestMapping("users")
public class UserController {

    private static final String USER_ID_VALIDATION_ERROR = "Invalid userId provided: ";
    private static final String USER_ID_DUP_ERROR = "UserId already provisioned in system: ";

    @Autowired
    private UserServiceImpl userService;

    @RequestMapping("")
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(userService.getAllUser(), HttpStatus.OK);
    }


    @RequestMapping("{userId}")
    public ResponseEntity<User> getUser(@PathVariable String userId) {

        User user;

        try {
            user = userService.getUser(userId);
        } catch (NotFoundException e) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, USER_ID_VALIDATION_ERROR + userId);
        } catch (Exception e) {
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }

        return new ResponseEntity<>(user, HttpStatus.FOUND);
    }


    @RequestMapping("{userId}/readArticles")
    public List<String> readArticles(@PathVariable String userId) {

        User user;

        try {
            user = userService.getUser(userId);
        } catch (NotFoundException e) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, USER_ID_VALIDATION_ERROR + userId);
        } catch (Exception e) {
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }

        return user.getReadArticles();
    }


    @RequestMapping(value = "{userId}/readArticles/{contentId}", method = RequestMethod.POST)
    public ResponseEntity readArticles(@PathVariable String userId, @PathVariable String contentId) {

        try {
            userService.addReadArticle(userId, contentId);
        } catch (NotFoundException e) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, USER_ID_VALIDATION_ERROR + userId);
        } catch (ApplicationException e) {
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (Exception e) {
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }

        return new ResponseEntity<String>(HttpStatus.CREATED);
    }


    @RequestMapping("{userId}/likedArticles")
    public ResponseEntity<List<String>> likedArticles(@PathVariable String userId) {

        User user;

        try {
            user = userService.getUser(userId);
        } catch (NotFoundException e) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, USER_ID_VALIDATION_ERROR + userId);
        } catch (Exception e) {
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }

        return new ResponseEntity<>(user.getLikedArticles(), HttpStatus.FOUND);
    }


    @RequestMapping(value = "{userId}/likedArticles/{contentId}", method = RequestMethod.POST)
    public ResponseEntity likeArticles(@PathVariable String userId, @PathVariable String contentId) {

            try {
                userService.addLikedArticle(userId, contentId);
            } catch (NotFoundException e) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, USER_ID_VALIDATION_ERROR + userId);
            } catch (ApplicationException e) {
                throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
            } catch (Exception e) {
                throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
            }

        return new ResponseEntity<String>(HttpStatus.CREATED);
    }


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createUser(@RequestBody User user) {

        try {
            userService.createUser(user);
        } catch (ValidationException e) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, USER_ID_DUP_ERROR + user.getUid());
        } catch (Exception e) {
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(user.getUid()).toUri());

        return new ResponseEntity<String>(null, headers, HttpStatus.CREATED);
    }


    @RequestMapping(value = "{userId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteUser(@PathVariable String userId) {

        try {
            userService.deleteUser(userId);
        } catch (ValidationException e) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, USER_ID_VALIDATION_ERROR + userId);
        } catch (Exception e) {
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }

        return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
    }
}
