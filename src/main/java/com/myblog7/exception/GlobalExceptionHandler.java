package com.myblog7.exception;

import com.myblog7.payload.ErrorDetails;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice//used for handling exceptions in the spring boot project.
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    //@ExceptionHandler will handle that exception.
    @ExceptionHandler(ResourceNotFound.class)//THE RESPONSE ENTITY : IT RETURNS THE RESPONSE ENTITY IN POSTMAN.WHATEVER RESPONSE WE WANT IN POSTMAN WE DID USING THIS.
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFound exception , WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
                webRequest.getDescription(false));//This line will give which URL the exception is occured.
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);//this will return status not found.
    }
    //THIS WILL HANDLE ALL EXCEPTION IN OUR PROJECT
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception exception,
                                                              WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
//when we used @Controller layer it acts as a controller layer between view and the backend business logic.