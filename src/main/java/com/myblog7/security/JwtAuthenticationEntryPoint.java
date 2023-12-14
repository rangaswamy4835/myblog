package com.myblog7.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {//entrypoint means it validates and checks incomng HTTP request if it is
    //unauthorized this class will throw exception .
    @Override
    public void commence(HttpServletRequest request,//Represents the incoming HTTP requests that triggered the authentication failure.
                         HttpServletResponse response,//Represents the HTTP response that will be sent back to the client.
                         AuthenticationException authException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,//if it is unauthorized send the resonse back,so thats
                //the reason here we are using response class to send the response.
                authException.getMessage());//authException means ,, what response we want to send that exception is present in this now.To send back the msg of exception.
    }
}