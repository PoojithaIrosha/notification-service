package com.poojithairosha.notification.repository;

import com.poojithairosha.notification.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndPassword(String username, String password);

    Optional<User> findByToken(String token);

}
