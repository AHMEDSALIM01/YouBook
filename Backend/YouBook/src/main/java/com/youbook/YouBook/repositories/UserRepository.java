package com.youbook.YouBook.repositories;

import com.youbook.YouBook.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users,Long>{
}
