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
        List<User> userList = entityManager.createQuery("SELECT U FROM User U join fetch U.role r", User.class)
                .getResultList();
        entityManager.close();
        return userList;
    }


    @Override
    public User getUser(Long id) {
        EntityManager entityManager = Persistence.create();
        User user = entityManager.find(User.class, id);
        entityManager.close();
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


}
