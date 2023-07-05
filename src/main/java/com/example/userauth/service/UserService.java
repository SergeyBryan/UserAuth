package com.example.userauth.service;

import com.example.userauth.entity.Role;
import com.example.userauth.entity.User;

import java.util.List;

public interface UserService {


    void createAccount(String name, String login, String password, Role... role);

    String authStatus(String name, String login, String password);


    void update(Long id, User user);

    void delete(Long id);

    List<User> getAllUser();

    List<User> getUserByRole(String name);

    User getUser(Long id);
}
