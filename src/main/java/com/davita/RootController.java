package com.davita;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kmasood on 7/1/16.
 */
@RestController
public class RootController {

    @RequestMapping("")
    public @ResponseBody String content() {
        return ("Welcome to DocLink!!!");
    }
}

