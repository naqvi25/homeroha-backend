package com.homeroha.repository;

import com.homeroha.model.Home;
import org.springframework.data.jpa.repository.JpaRepository;

// import com.homeroha.model.User;
// import com.homeroha.model.UserHome;

// import java.util.List;

public interface HomeRepository extends JpaRepository<Home, Long> {
//    List<UserHome> findAllByUser(User user);

}
