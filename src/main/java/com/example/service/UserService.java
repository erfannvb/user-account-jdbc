package com.example.service;

import com.example.entity.User;
import com.example.entity.enumeration.Gender;
import com.example.entity.enumeration.UserRole;
import com.example.repository.user.UserRepositoryImpl;

import java.util.regex.Pattern;

public class UserService {

    private final UserRepositoryImpl userRepository = new UserRepositoryImpl();

    public void saveUser(String firstName, String lastName, String email, UserRole userRole,
                         String username, Gender gender, int age) {

        if (Pattern.matches("[A-Z][a-z]*", firstName) ||
                Pattern.matches("[A-Z][a-z]*", lastName) ||
                Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", email)) {

            User user = new User();

            user.setUserId(userIdGenerator());
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setUserRole(userRole);
            user.setUsername(username);
            user.setGender(gender);
            user.setAge(age);

            userRepository.save(user);

        } else {
            System.out.println("Invalid input!!!");
            System.exit(0);
        }
    }

    public void printUserInfo() {
        User[] users = userRepository.loadAllUsingArray();
        for (User user : users) {
            System.out.println(user.getFirstName() + " " + user.getLastName());
        }
    }

    private long userIdGenerator() {
        long generator = userRepository.loadId() + 1;
        if (generator == 0) generator = 1;
        return generator;
    }

}
