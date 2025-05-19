package com.homeroha.repository;

import com.homeroha.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
//    Optional<User> findByEmailWithActiveHome(String email);
}
