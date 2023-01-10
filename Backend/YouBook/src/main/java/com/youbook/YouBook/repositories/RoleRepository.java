package com.youbook.YouBook.repositories;

import com.youbook.YouBook.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@Repository
@CrossOrigin("**")
public interface RoleRepository extends JpaRepository<Role,Long>{
    Role findByName(String name);
}
