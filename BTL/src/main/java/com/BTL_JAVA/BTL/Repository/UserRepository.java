package com.BTL_JAVA.BTL.Repository;

import com.BTL_JAVA.BTL.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByFullName(String username);
    Optional<User> findByFullName(String fullName);

}
