package com.davita.model;

import com.davita.model.Content;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by kmasood on 7/1/16.
 */
public interface ContentRepository extends CrudRepository<Content, String> {
}
