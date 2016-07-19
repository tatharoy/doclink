package com.davita;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by kmasood on 7/1/16.
 */
public interface UserRepository  extends CrudRepository<User, String> {
    Optional<User> findByUid(String id);
    Optional<List<User>> findByName(String name);
}
