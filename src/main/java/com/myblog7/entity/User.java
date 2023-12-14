package com.myblog7.entity;

import lombok.Data;


import javax.persistence.*;
import java.util.Set;


    @Data
    @Entity
    @Table(name = "users", uniqueConstraints = {
            @UniqueConstraint(columnNames = {"username"}),
            @UniqueConstraint(columnNames = {"email"})
    })
    public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;
        private String name;
        private String username;
        private String email;
        private String password;
        @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)//first many for user and second one for Role.
        //the moment i loaded the project all these tables will be loaded into cache memory.(cascadeType.All = meaning we make the changes in one table it will reflect
        //in the other table.
        @JoinTable(name = "user_roles",//this is where we give the table names.
                joinColumns = @JoinColumn(name = "user_id", referencedColumnName
                        = "id"),//user id is the foreign key in the third table which is reference to primary key of parent table user.
                inverseJoinColumns = @JoinColumn(name = "role_id",
                        referencedColumnName = "id"))//role_id is the foreign in the third table which is now reference to the primary key of role table.
        private Set<Role> roles;//when it is many to many we will use set bcz we don't want duplicate values.if  i use list duplicate values will be allowed
    }

//How do you join two tables
//to join two tables we will be using @JoinTable(name=user_roles)so that third table get created