package com.homeroha.repository;

import com.homeroha.model.User;
import com.homeroha.model.UserHome;
import com.homeroha.model.Home;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserHomeRepository extends JpaRepository<UserHome, Long> {
//    List<UserHome> findByUser(User user);
    List<UserHome> findAllByUser(User user);
    boolean existsByUserAndHome(User user, Home home);
    Optional<UserHome> findByUserAndHome(User user, Home home);
    List<UserHome> findAllByHome(Home home);
}
