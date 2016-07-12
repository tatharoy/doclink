package com.davita;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by kmasood on 7/1/16.
 */
public interface UserRepository  extends CrudRepository<User, String> {
    User findByUid(String id);
    List<User> findByName(String name);
}
