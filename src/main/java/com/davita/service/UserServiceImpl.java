package com.davita.service;

import com.davita.exception.ApplicationException;
import com.davita.exception.NotFoundException;
import com.davita.exception.ValidationException;
import com.davita.model.Content;
import com.davita.model.User;
import com.davita.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kmasood on 7/20/16.
 */
@Service
public class UserServiceImpl implements UserService {
    private static final String USER_ID_VALIDATION_ERROR = "Invalid userId provided: ";
    private static final String USER_ID_DUP_ERROR = "UserId already provisioned in system: ";
    private static final String CONTENT_ID_VALIDATION_ERROR = "Invalid contentId provided: ";

    @Autowired
    private ContentService contentService;

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUser() {

        List<User> userList = new ArrayList<>();

        Iterable<User> users = userRepository.findAll();

        for (User user: users) {
            userList.add(user);
        }

        return userList;
    }


    @Override
    public User getUser(String userId) throws NotFoundException {

        User user = userRepository.findByUid(userId).orElseThrow(() ->
                new NotFoundException(USER_ID_VALIDATION_ERROR + userId));

        return user;
    }


    @Override
    public List<String> getReadArticles(String userId) throws NotFoundException {

        User user = userRepository.findByUid(userId).orElseThrow(() ->
                new NotFoundException(USER_ID_VALIDATION_ERROR + userId));

        return user.getReadArticles();
    }


    @Override
    public void addReadArticle(String userId, String contentId) throws NotFoundException, ApplicationException {

        Content content = contentService.getContent(contentId);

        if (content != null) {

            User user = userRepository.findByUid(userId).orElseThrow(() ->
                    new NotFoundException(USER_ID_VALIDATION_ERROR + userId));

            user.addReadContent(content.getId());
            userRepository.save(user);
        } else {
            new NotFoundException(CONTENT_ID_VALIDATION_ERROR + contentId);
        }
    }


    @Override
    public List<String> getLikedArticles(String userId) throws NotFoundException {

        User user = userRepository.findByUid(userId).orElseThrow(() ->
                new NotFoundException(USER_ID_VALIDATION_ERROR + userId));

        return user.getLikedArticles();
    }


    @Override
    public void addLikedArticle(String userId, String contentId) throws NotFoundException, ApplicationException {

        Content content = contentService.getContent(contentId);

        if (content != null) {

            User user = userRepository.findByUid(userId).orElseThrow(() ->
                    new NotFoundException(USER_ID_VALIDATION_ERROR + userId));

            user.addlikedContent(content.getId());
            userRepository.save(user);
        } else {
            throw new NotFoundException(CONTENT_ID_VALIDATION_ERROR + contentId);
        }
    }


    @Override
    public URI createUser(User user) throws ValidationException {

        if (userRepository.findByUid(user.getUid()).isPresent()) {
            throw new ValidationException(USER_ID_DUP_ERROR + user.getUid());
        } else {
            userRepository.save(user);
        }

        URI locationURI = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(user.getUid()).toUri();

        return locationURI;
    }


    @Override
    public void deleteUser(String userId) throws ValidationException {

        User user = userRepository.findByUid(userId).orElseThrow(() ->
                new ValidationException(USER_ID_VALIDATION_ERROR + userId));

            userRepository.delete(user);
    }


}
