package com.volunteerplatform.data;

import com.volunteerplatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    @Query("select u from User u left join fetch u.favouriteMentorings where u.id = :id")
    Optional<User> findByIdWithFavouriteMentorings(@Param("id") Long id);


}