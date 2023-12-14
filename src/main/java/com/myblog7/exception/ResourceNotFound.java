package com.myblog7.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFound extends RuntimeException {// In order to build the custom exception firstly we need to make the sub class of the exception ,here sub class of run time exception
    public ResourceNotFound(String msg){ // when we create a object of resourcenotfound supply the message to its constructor line 4 is the constructor
        super(msg); //Will automatically display the message in postman Response.
    }

}
