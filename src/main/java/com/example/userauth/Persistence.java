package com.example.userauth;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class Persistence {

    private static final EntityManagerFactory entityManagerFactory = javax.persistence.Persistence.createEntityManagerFactory("myPersistenceUnit");

    public static EntityManager create() {
        return entityManagerFactory.createEntityManager();
    }

}
