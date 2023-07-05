package com.example.userauth;

import com.example.userauth.entity.Role;
import com.example.userauth.entity.User;
import com.example.userauth.service.RoleServiceImpl;
import com.example.userauth.service.UserServiceImpl;

import java.util.Arrays;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();
        RoleServiceImpl roleService = new RoleServiceImpl();
        Role[] listOfROles = {
                new Role(1L, "Разработчик"),
                new Role(2L, "Аналитик"),
                new Role(3L, "Тестировщик"),
                new Role(4L, "Менеджер"),
                new Role(5L, "Дизайнер"),
                new Role(6L, "По умолчанию")
        };
        Arrays.stream(listOfROles).forEach(roleService::create);
        List<Role> allRoles = roleService.getAllRoles();
        printRoleInfo(allRoles);
        allRoles.forEach(role -> roleService.delete(role.getId()));
        printRoleInfo(roleService.getAllRoles());
        userService.createAccount("name", "login", "password", listOfROles[0], listOfROles[1]);
        System.out.println(userService.authStatus("name", "login", "password"));
        System.out.println("Поиска юзера по роли");
        userService.getUserByRole("Аналитик");
        System.out.println("______________");
        System.out.println("все пользователи");
        userService.getAllUser();
        List<User> users = userService.getAllUser();
        System.out.println("По юзер id");
        userService.getUser(users.get(0).getId());
        List<User> user = userService.getAllUser();
        User newUser = user.get(0);
        newUser.setName("Frog");
        userService.update(user.get(0).getId(), newUser);
        userService.getAllUser();
        userService.delete(newUser.getId());
    }


    private static void printRoleInfo(List<Role> list) {
        for (Role role : list) {
            System.out.println("role: " + role.getName());
            for (User user : role.getUserList()) {
                System.out.println("login: " + user.getLogin());
                System.out.println("password: " + user.getPassword());
                System.out.println("name: " + user.getName());
                System.out.println("Date: " + user.getCreateDate());
            }
        }
    }
}