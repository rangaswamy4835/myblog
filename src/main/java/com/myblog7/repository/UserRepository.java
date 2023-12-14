package com.myblog7.repository;




import com.myblog7.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);//this method will find by emailid returns back an optional object.
    Optional<User> findByUsernameOrEmail(String username, String email);//it will find by username or emailid.
    Optional<User> findByUsername(String username);//it will find by username.
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

}