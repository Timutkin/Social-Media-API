package ru.timutkin.socialmediaapi.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.timutkin.socialmediaapi.storage.entity.UserEntity;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    UserEntity getById(Long id);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}