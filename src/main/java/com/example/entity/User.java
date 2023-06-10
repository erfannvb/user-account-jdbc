package com.example.entity;


import com.example.entity.enumeration.Gender;
import com.example.entity.enumeration.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

    private Long userId;

    private String firstName;

    private String lastName;

    private String email;

    private UserRole userRole;

    private String username;

    private Gender gender;

    private int age;

}
