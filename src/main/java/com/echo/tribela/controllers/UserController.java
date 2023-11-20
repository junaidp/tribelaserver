package com.echo.tribela.controllers;

import com.echo.tribela.requests.SignUpRequest;
import com.echo.tribela.services.UserService;
import com.echo.tribela.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/Customer")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
   ResponseEntity<ApiResponse> signUp(
            @Valid @RequestBody SignUpRequest request){
       return userService.saveUser(userService.map(request));
    }

}
