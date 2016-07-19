package com.davita;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kmasood on 7/1/16.
 */
@RestController
@RequestMapping("users")
public class UserController {

    private static final String USER_ID_VALIDATION_ERROR = "Invalid userId provided: ";
    private static final String USER_ID_DUP_ERROR = "UserId already provisioned in system: ";
    private static final String CONTENT_ID_VALIDATION_ERROR = "Invalid contentId provided: ";

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("")
    public ResponseEntity<List<User>> getUsers() {

        List<User> userList = new ArrayList<>();

        Iterable<User> users = userRepository.findAll();

        for (User user: users) {
            userList.add(user);
        }

        return new ResponseEntity<>(userList, HttpStatus.OK);
    }


    @RequestMapping("{userId}")
    public ResponseEntity<User> getUser(@PathVariable String userId) {

        User user = userRepository.findByUid(userId).orElseThrow(() ->
                new HttpClientErrorException(HttpStatus.NOT_FOUND, USER_ID_VALIDATION_ERROR + userId));

        return new ResponseEntity<>(user, HttpStatus.FOUND);
    }


    @RequestMapping("{userId}/readArticles")
    public List<String> readArticles(@PathVariable String userId) {

        User user = userRepository.findByUid(userId).orElseThrow(() ->
                new HttpClientErrorException(HttpStatus.NOT_FOUND, USER_ID_VALIDATION_ERROR + userId));

        return user.getReadArticles();
    }


    @RequestMapping(value = "{userId}/readArticles/{contentId}", method = RequestMethod.POST)
    public ResponseEntity readArticles(@PathVariable String userId, @PathVariable String contentId) {

        Content content = contentRepository.findOne(contentId);

        if (content != null) {

            User user = userRepository.findByUid(userId).orElseThrow(() ->
                    new HttpClientErrorException(HttpStatus.NOT_FOUND, USER_ID_VALIDATION_ERROR + userId));

            user.addReadContent(content.getId());
            userRepository.save(user);
        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, CONTENT_ID_VALIDATION_ERROR + contentId);
        }

        return new ResponseEntity<String>(HttpStatus.CREATED);
    }


    @RequestMapping("{userId}/likedArticles")
    public ResponseEntity<List<String>> likedArticles(@PathVariable String userId) {

        User user = userRepository.findByUid(userId).orElseThrow(() ->
                new HttpClientErrorException(HttpStatus.NOT_FOUND, USER_ID_VALIDATION_ERROR + userId));

        return new ResponseEntity<>(user.getLikedArticles(), HttpStatus.FOUND);
    }


    @RequestMapping(value = "{userId}/likedArticles/{contentId}", method = RequestMethod.POST)
    public ResponseEntity likeArticles(@PathVariable String userId, @PathVariable String contentId) {

        Content content = contentRepository.findOne(contentId);

        if (content != null) {

            User user = userRepository.findByUid(userId).orElseThrow(() ->
                    new HttpClientErrorException(HttpStatus.NOT_FOUND, USER_ID_VALIDATION_ERROR + userId));

            user.addlikedContent(content.getId());
            userRepository.save(user);
        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, CONTENT_ID_VALIDATION_ERROR + contentId);
        }

        return new ResponseEntity<String>(HttpStatus.CREATED);
    }


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createUser(@RequestBody User user) {

        if (userRepository.findByUid(user.getUid()).isPresent()) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, USER_ID_DUP_ERROR + user.getUid());
        } else {
            userRepository.save(user);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(user.getUid()).toUri());

        return new ResponseEntity<String>(null, headers, HttpStatus.CREATED);
    }


    @RequestMapping(value = "{userId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteUser(@PathVariable String userId) {

        User user = userRepository.findByUid(userId).orElseThrow(() ->
                new HttpClientErrorException(HttpStatus.NOT_FOUND, USER_ID_VALIDATION_ERROR + userId));

        userRepository.delete(user);

        return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
    }
}
