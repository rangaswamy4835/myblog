package com.myblog7.payload;

import lombok.Data;

@Data
public class LoginDto {
    private String usernameOrEmail;
    private String password;
}
//LoginDto will all it will do is from the JSON object it will take the data into the java object.