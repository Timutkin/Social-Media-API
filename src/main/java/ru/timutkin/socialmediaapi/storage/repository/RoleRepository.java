package ru.timutkin.socialmediaapi.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.timutkin.socialmediaapi.storage.entity.RoleEntity;
import ru.timutkin.socialmediaapi.storage.enumeration.Role;


@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity findByRole(Role role);
}
