package com.echo.tribela.services;


import com.echo.tribela.models.User;
import com.echo.tribela.repository.UserRepository;
import com.echo.tribela.requests.SignUpRequest;
import com.echo.tribela.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private PasswordEncoder passwordEncoder;

    UserRepository userRepository = null;

   public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User map(SignUpRequest signUpRequest){
        return User.newInstance(
                signUpRequest.getName(),
                signUpRequest.getEmail(),
                signUpRequest.getAddress(),
                passwordEncoder.encode(signUpRequest.getPassword()), "user");
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
        User user = userRepository.findByUsername(username);

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        List<GrantedAuthority> auth = AuthorityUtils
                .commaSeparatedStringToAuthorityList("USER");
        if (username.equals("admin")) {
            auth = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getAuthorities()
                        .stream()
                        .map(role-> new SimpleGrantedAuthority("user"))
                        .collect(Collectors.toSet()));

    }
}
