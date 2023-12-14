package com.myblog7.security;

import com.myblog7.exception.BlogAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
public class JwtAuthenticationFilter extends OncePerRequestFilter { //The JwtAuthenticationFilter class plays a critical role in a Spring Security configuration for handling JSON Web Token (JWT) based authentication in a web application. Its purpose is to intercept incoming HTTP requests, extract JWTs from the request headers, validate those tokens, and establish authentication for the requests based on the contents of the tokens. Here's a breakdown of its purpose and key components:
    // inject dependencies
    //login request will common for all this will not go to the entry point class bcz login page is same for all.
    //when we are logging in that request should not go the JWT entry point the request will come to the filter class
    //firstly it will pick up the JWT token.when we login then only token will be generated then subsequent request will go to the token
    //like when we login to gmail it gives me inbox.compose request is going with token first filter class
    //will come bcz filterclass is the one that intercepts thr requests gets the token validate the token to understand wheather it's a authenticated user and the role of that user this is going to
    //study that if it is invalid then it will tell token is invalid if it is valid then it will do some
    //setting in the spring security context so that we don't recquire to login again.
    //usually when we login token is generated this is nothing to do filter class or entrypointclass .
    //when second request when you send the token is send with the request filterclass will extract the token
    //then the token is verified wheather it is valid or not then it will check the role of the otp.then
    //it will send to the security context there is area in the spring security and in that it saves with role now the role info is to be set now the url is being check by the entry point
    //can i get the acces or not.
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // get JWT (token) from http request
        String token = getJWTfromRequest(request);
        // validate token
        try {
            if(StringUtils.hasText(token) && tokenProvider.validateToken(token)){
                // get username from token
                String username = tokenProvider.getUsernameFromJWT(token);
                // load user associated with token
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken = new
                        UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authenticationToken.setDetails(new
                        WebAuthenticationDetailsSource().buildDetails(request));
                // set spring security
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (BlogAPIException e) {
            throw new RuntimeException(e);
        }
        filterChain.doFilter(request, response);
    }
    // Bearer <accessToken>
    private String getJWTfromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
