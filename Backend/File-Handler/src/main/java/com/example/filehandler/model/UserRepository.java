package com.example.filehandler.model;

import com.example.filehandler.repository.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    // get user id by username
    int findUserIdByUsername(String username);

}
