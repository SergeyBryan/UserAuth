package com.example.userauth.service;

import com.example.userauth.entity.Role;

import java.util.List;

public interface RoleService {

    void update(Long id, Role role);

    void delete(Long id);

    void create(Role role);

    List<Role> getAllRoles();

    Role getRole(Long id);

}
