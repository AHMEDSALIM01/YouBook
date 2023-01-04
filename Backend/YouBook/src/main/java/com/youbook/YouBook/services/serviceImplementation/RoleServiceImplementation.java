package com.youbook.YouBook.services.serviceImplementation;

import com.youbook.YouBook.entities.Role;
import com.youbook.YouBook.repositories.RoleRepository;
import com.youbook.YouBook.services.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImplementation implements RoleService {
    private RoleRepository roleRepository;

    public RoleServiceImplementation(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role addRole(Role role) {
        return null;
    }

    @Override
    public Role updateRole(Role role) {
        return null;
    }

    @Override
    public Role deleteRole(Role role) {
        return null;
    }

    @Override
    public List<Role> getAllRoles() {
        return null;
    }

    @Override
    public Role getRoleBYId(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        if(!role.isPresent()){
            throw  new IllegalStateException("role non trouvée");
        }
        return role.get();
    }

    @Override
    public Role getRoleByName(String name) {
        Role role = roleRepository.findByName(name);
        if(role == null){
            throw  new IllegalStateException("role non trouvée");
        }
        return role;
    }
}
