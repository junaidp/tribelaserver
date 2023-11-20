package com.echo.tribela.repository;


import com.echo.tribela.models.User;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

//@Repository
public interface UserRepository extends MongoRepository<User, String> {
    //User findByUsername(String username);
}