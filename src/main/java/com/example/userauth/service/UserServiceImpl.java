package com.example.userauth.service;

import com.example.userauth.Persistence;
import com.example.userauth.entity.Role;
import com.example.userauth.entity.User;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public void createAccount(String name, String login, String password, Role... role) {
        try {
            User user = create(name, login, password, role);
            EntityManager entityManager = Persistence.create();
            entityManager.getTransaction().begin();
            entityManager.persist(user);
            entityManager.getTransaction().commit();
            entityManager.close();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Пользователь с таким именем уже существует");
        }
    }


    @Override
    public String authStatus(String name, String login, String password) {
        User user = this.getAllUser().stream().filter(user1 -> user1.getLogin().equals(login) & user1.getPassword().equals(password) & user1.getName().equals(name)).findAny().orElse(null);
        return user == null ? "Аутентификация не удалась" : "Добро пожаловать, " + user.getName();
    }


    @Override
    public void update(Long id, User user) {
        EntityManager entityManager = Persistence.create();
        entityManager.getTransaction().begin();
        user.setModifiedTime(LocalDateTime.now());
        user.setCreateDate(entityManager.find(User.class, id).getCreateDate());
        entityManager.merge(user);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void delete(Long id) {
        EntityManager entityManager = Persistence.create();
        entityManager.getTransaction().begin();
        User user = entityManager.find(User.class, id);
        entityManager.remove(user);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public List<User> getAllUser() {
        EntityManager entityManager = Persistence.create();
        List<User> userList = entityManager.createQuery("SELECT U FROM User U "
                        , User.class)
                .getResultList();
        entityManager.close();
        printAllUsers(userList);
        return userList;
    }

    @Override
    public List<User> getUserByRole(String name) {
        EntityManager entityManager = Persistence.create();
        List<User> userList = entityManager.createQuery("SELECT U FROM User U join fetch U.role r WHERE r.name=:name", User.class)
                .setParameter("name", name)
                .getResultList();
        entityManager.close();
        printAllUsers(userList);
        return userList;
    }


    @Override
    public User getUser(Long id) {
        EntityManager entityManager = Persistence.create();
        User user = entityManager.createQuery("SELECT U FROM User U join fetch U.role r WHERE U.id =:id", User.class).setParameter("id", id).getSingleResult();
        entityManager.close();
        printUser(user);
        return user;
    }

    private User create(String login, String password, String name, Role... role) {
        User user = new User(login, password, name);
        user.setCreateDate(LocalDateTime.now());
        user.setRole(Arrays.stream(role).toList());
        if (dataBaseChecker(user.getName())) {
            return null;
        } else {
            return user;
        }
    }

    private boolean dataBaseChecker(String name) {
        EntityManager entityManager = Persistence.create();
        List<User> userList = entityManager.createQuery("SELECT U FROM User U", User.class)
                .getResultList();
        entityManager.close();
        return userList.stream().anyMatch(user -> user.getName().equals(name));
    }

    private void printUser(User user) {
        System.out.println("name: " + user.getName());
        System.out.println("login: " + user.getLogin());
        System.out.println("password: " + user.getPassword());
        System.out.println("Created Date: " + user.getCreateDate());
        System.out.println("Modified Date: " + user.getModifiedTime());
        for (Role role : user.getRole()) {
            System.out.println("role: " + role.getName());
        }
    }

    private void printAllUsers(List<User> list) {
        for (User user : list) {
            System.out.println("name: " + user.getName());
            System.out.println("login: " + user.getLogin());
            System.out.println("password: " + user.getPassword());
            System.out.println("Created Date: " + user.getCreateDate());
            System.out.println("Modified Date: " + user.getModifiedTime());
        }
    }
}
