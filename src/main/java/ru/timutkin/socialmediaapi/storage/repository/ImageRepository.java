package ru.timutkin.socialmediaapi.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.timutkin.socialmediaapi.storage.entity.ImageEntity;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
}