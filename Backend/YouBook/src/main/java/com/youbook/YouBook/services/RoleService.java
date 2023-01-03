package com.youbook.YouBook.services;

import com.youbook.YouBook.entities.Role;

import java.util.List;

public interface RoleService {
    Role addRole(Role role);
    Role updateRole(Role role);
    Role deleteRole(Role role);
    List<Role> getAllRoles();
    Role getRoleBYId(Long id);
    Role getRoleByName(String name);
}
