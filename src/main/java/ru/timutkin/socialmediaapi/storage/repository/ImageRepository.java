package ru.timutkin.socialmediaapi.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.timutkin.socialmediaapi.storage.entity.ImageEntity;
import ru.timutkin.socialmediaapi.storage.repository.projection.ImageOnlyBytes;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {

    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM ImageEntity ime WHERE ime.id =:id ")
    void deleteById(@Param("id") Long id);

    @Query("SELECT im.image as image FROM ImageEntity im WHERE im.id = :imId")
    Optional<ImageOnlyBytes> getByImageId(@Param("imId") Long imId);
}