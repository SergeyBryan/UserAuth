package com.example.userauth.service;

import com.example.userauth.Persistence;
import com.example.userauth.entity.Role;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Override
    public void update(Long id, Role role) {
        role.setId(id);
        EntityManager entityManager = Persistence.create();
        entityManager.getTransaction().begin();
        entityManager.merge(role);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void delete(Long id) {
        EntityManager entityManager = Persistence.create();
        entityManager.getTransaction().begin();
        Role role = entityManager.find(Role.class, id);
        entityManager.remove(role);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void create(Role role) {
        EntityManager entityManager = Persistence.create();
        entityManager.getTransaction().begin();
        entityManager.persist(role);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public List<Role> getAllRoles() {
        EntityManager entityManager = Persistence.create();
        List<Role> resultList = entityManager.createQuery("SELECT R FROM Role R left join fetch R.userList", Role.class)
                .getResultList();
        entityManager.close();
        return resultList;
    }

    @Override
    public Role getRole(Long id) {
        EntityManager entityManager = Persistence.create();
        Role role = entityManager.find(Role.class, id);
        entityManager.close();
        return role;
    }
}
