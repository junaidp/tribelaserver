package com.echo.tribela.services;

import com.echo.tribela.models.User;
import com.echo.tribela.repository.UserRepository;
import com.echo.tribela.requests.SignUpRequest;
import com.echo.tribela.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Service
public class UserService implements UserDetailsService {
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User map(SignUpRequest signUpRequest){
        return User.newInstance(
                signUpRequest.getName(),
                signUpRequest.getEmail(),
                signUpRequest.getAddress(),
                passwordEncoder.encode(signUpRequest.getPassword()));
   }

    public ResponseEntity<ApiResponse> saveUser(User user){

        try {
            userRepository.save(user);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(user)
                    .toUri();

            return ResponseEntity.created(location).body(new ApiResponse(true, "registered successfully", "", HttpStatus.OK));

        }
        catch(Exception ex){
            return ResponseEntity.badRequest().body(new ApiResponse(false, "registered Unsuccessfully", ex.getMessage(), HttpStatus.BAD_REQUEST));

        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //UserDetails user = new UserDetails();
        return null;
    }
}
