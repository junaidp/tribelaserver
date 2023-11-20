package com.echo.tribela.models;

import lombok.*;

import javax.persistence.*;


@AllArgsConstructor
@EqualsAndHashCode(of = {"name", "email", "address", "password"})
@Getter
@NoArgsConstructor
@Setter
@Table(name = "user")
@ToString(of = {"name", "email", "address", "address"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String password;

    public static User newInstance(String name, String email, String address, String password) {
        return new User(
                Long.parseLong("1"),
                name,
                email,
                address,
                password
        );
    }
}
