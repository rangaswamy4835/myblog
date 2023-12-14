package com.myblog7.controller;

import com.myblog7.payload.PostDto;
import com.myblog7.payload.PostResponse;
import com.myblog7.service.PostService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostController {
    private PostService postService;

    public PostController( PostService postService) {

        this.postService = postService;
    }
    //http://localhost:8080/api/post
    @PreAuthorize("hasRole('ADMIN')")//this will help us to access only by admin
    @PostMapping
    // if use ? it would be generic we can return any type of statement in the below line
    //@Valid if we don't use in the controller layer error checking will not happen @Valid that enables that enables the error checking in the controller layer.
    //if errors are there in binding result there is a method haserrors if haserror has true it means there is some error validation error that validation error we are returning it back getDefaultMessage.
    public ResponseEntity<?> savePost(@Valid  @RequestBody PostDto postDto, BindingResult result){ //Bcz of @RequestBody from the json the content is copied to postDto
         if(result.hasErrors()) {                                                                  // If we miss the @RequestBody the data present in the postman will not go.
             return new ResponseEntity<>(result.getFieldError().getDefaultMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
         }
        PostDto dto = postService.savePost(postDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);//sTATUS CODE 201 ,,When ever we are saving the record the status code is 201
    }
    //http://localhost:8080/api/post/1
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") long id){
        postService.deletePost(id);
        return new ResponseEntity<>("Post is deleted",HttpStatus.OK);// FOR deletion updation and reading status code is 200
                                                                          //Unable to login , wrong username and password ,401 forbidden ,and any logical problem 500 internal server error
    }

    //http://localhost:8080/api/post/1
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
        public ResponseEntity<PostDto> updatePost(@PathVariable("id") long id,@RequestBody PostDto postDto){
        PostDto dto =  postService.updatePost(id,postDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
        }
    //http://localhost:8080/api/post/1
        @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable("id") long id){
        PostDto dto =  postService.getPostById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    //http://localhost:8080/api/post?pageNo=0&pageSize=5&sortBy=title&sortDir=desc  // page no 0 means first page the next page will at index 1 then 2 , if give page no 0 then it will the data of page no 0
    //if use the slash then it will become pathvariable ,, if use requestParam then it should have a ?,cannot have a slash Bcz it's a query parameter if i am using requestParam.
    //Imagine if the page has total 6 records, first page 5 records second page 1 record.now if we want read that now we need to use requestParam
    @GetMapping
    public PostResponse getPosts(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo, //this required false is not mandatory
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
            //What is this default value ,  if we dont give page no in the url then it will be default page 1,, if we given the default value in the url then the default value will be not taken from the above code
    //Here we used required false in the code if the page no is given in the url it doesn't matter
    ) {





        PostResponse postResponse = postService.getPosts(pageNo,pageSize,sortBy,sortDir);
        return postResponse;
    }

}
//Post man calls controller controller calls service , service back to controller , controller backs to postman with the message