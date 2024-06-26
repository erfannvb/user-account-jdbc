package com.example.repository.user;

import com.example.entity.User;

import java.util.List;

public interface UserRepository {

    void save(User user);

    long saveAndReturnUserId(User user);

    void saveAll(List<User> userList);

    void update(User user);

    void deleteById(Long userId);

    User loadById(Long userId);

    List<User> loadAll();

    User[] loadAllUsingArray();

    int getNumberOfUsers();

    long loadId();

}
