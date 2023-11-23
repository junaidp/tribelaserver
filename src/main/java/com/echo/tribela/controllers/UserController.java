package com.echo.tribela.controllers;

import com.echo.tribela.requests.LoginRequest;
import com.echo.tribela.requests.SignUpRequest;
import com.echo.tribela.services.UserService;
import com.echo.tribela.util.ApiResponse;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/Customer")
public class UserController {

    @Autowired
    UserService userService;

    private final AuthenticationManager authenticationManager;

    public UserController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping
   ResponseEntity<ApiResponse> signUp(
            @Valid @RequestBody SignUpRequest request){
       return userService.saveUser(userService.map(request));
    }

    @GetMapping("/test")
    public String test(){
        return "hello";
    }

    @GetMapping("/logout")
    public String logout(){
         return "logout";
    }

    SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    @PostMapping("/logout")
    public String performLogout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        // .. perform logout
        this.logoutHandler.logout(request, response, authentication);
        return "redirect:/home";
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest loginRequest) {
        Authentication authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getUsername(), loginRequest.getPassword());
        Authentication authenticationResponse =
                this.authenticationManager.authenticate(authenticationRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(authenticationResponse)
                .toUri();

        if(authenticationResponse.isAuthenticated())
            return ResponseEntity.created(location).body(new ApiResponse(true, "loggedIn successfully", "", HttpStatus.OK));
        else
            return ResponseEntity.created(location).body(new ApiResponse(false, "loggedIn failed", "", HttpStatus.OK));

    }

}
