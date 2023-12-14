package com.myblog7.security;
import com.myblog7.entity.Role;
import com.myblog7.entity.User;
import com.myblog7.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {//we will supply username to it automatically spring security takes the username from
    //the LoginDto objet based on that username it will go to the database and it will
    //search the record based on username or email either one it will check wheather this username exists in the database or email exists in the
    //database bcz repository layers are used to perform the database operations .if the
    //record is found the user object is initialized .if not it will throw exception usernotfound with username or password

        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email:" + usernameOrEmail));
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }
    private Collection< ? extends GrantedAuthority>
    mapRolesToAuthorities(Set<Role> roles){
        return roles.stream().map(role -> new
                SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
//What signin Does?
//it takes the username and password from the json and then authenticate method should verify login details with username and password
//through what it will verify
//with Database from load by Username
//load by username coming from custom user detail class that loads the username , internally it compares
//After comparing is this user enabled or not one more class is developed customuserdetailsService  class to which the user object is going
//now it will checks everything then it returns back customuserdetailService