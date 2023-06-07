package ru.timutkin.socialmediaapi.storage.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.timutkin.socialmediaapi.storage.entity.PostEntity;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
    @EntityGraph(value = "post-with-images", type = EntityGraph.EntityGraphType.FETCH)
    List<PostEntity> findAll();
    @EntityGraph(value = "post-with-images", type = EntityGraph.EntityGraphType.FETCH)
    Optional<PostEntity> findById(Long id);
    boolean existsByAuthorIdAndId(Long authorId, Long postId);
}