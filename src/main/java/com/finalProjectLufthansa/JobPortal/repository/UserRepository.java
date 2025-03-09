package com.finalProjectLufthansa.JobPortal.repository;


import com.finalProjectLufthansa.JobPortal.model.entity.User;
import com.finalProjectLufthansa.JobPortal.model.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Page<User> findAllByRole(Role role, Pageable pageable);
}