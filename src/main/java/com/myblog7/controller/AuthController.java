//To develop signup feature
package com.myblog7.controller;

import com.myblog7.entity.User;
import com.myblog7.payload.LoginDto;
import com.myblog7.payload.SignUpDto;
import com.myblog7.repository.UserRepository;
import com.myblog7.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private AuthenticationManager authenticationManager;
    //http://localhost:8080/api/auth/signin    ///Via this URL we will supply JSON object along with username and password,
    //that will go to LoginDto loginDto , the flow starts with username and password.
    //Authentication Manager?
    //
    @Autowired
    private JwtTokenProvider tokenProvider;
    @PostMapping("/signin")
    //this below line will help us to know that will send back response entity JWTtoken o response entity

    public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody LoginDto
                                                                    loginDto){
        Authentication authentication = authenticationManager.authenticate(new
                UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // get token form tokenProvider
        String token = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JWTAuthResponse(token));
    }
    //    @PostMapping("/signin")
//    public ResponseEntity<String> authenticateUser(@RequestBody LoginDto
//                                                           loginDto){
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword())
//        );//when ever we want to verify two things is recquired username and password ,in this Authenticate method we need to create
//        // a method newUserNamePasswordAuthenticationToken this is the builtin object present in the springSecurity this
//        //object takes two values email and password , this object what it takes it verifies , if the email and
//        //password is valid that generates a Token if it is not valid it will not generate a Token.The token
//        //is generated or not is pointed by authentication.if token is not generated also it is pointing to authentication.
//        SecurityContextHolder.getContext().setAuthentication(authentication);//Once we login , it will help me to
//        //remember the user for all the URLS .By setting up the context we will achieve this .
//        return new ResponseEntity<>("User signed-in successfully!.", HttpStatus.OK);
//    }
    //http://localhost:8080/api/auth/signup
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto) {
//when we write the ? thr return type can be anything it can be STRING it can be DTO it can be ENTITY anything.
// @RequestBody will copy the data from JSON dto.
       Boolean emailExist = userRepo.existsByEmail(signUpDto.getEmail());
       if(emailExist){
           return new ResponseEntity<>("email id exist", HttpStatus.BAD_REQUEST);//FOR THIS THE STATUS CODE IS 400
           //HttpStatus.InternalServerError// for this the status code is 500.

       }
       Boolean existUserName = userRepo.existsByUsername(signUpDto.getUsername());
       if(existUserName){
           return new ResponseEntity<>("user name Exist", HttpStatus.BAD_REQUEST);
       }




        User user = new User();//here the data is copied to user object.
        user.setName(signUpDto.getName());
        user.setEmail(signUpDto.getEmail());
        user.setUsername(signUpDto.getUsername());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));//Bcz password must be encrypted so we used passwordEncoder.
        userRepo.save(user);//THIS IS FOR SAVE BUTTON
        return new ResponseEntity<>("user is registered", HttpStatus.CREATED);
    }
}