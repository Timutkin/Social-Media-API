package ru.timutkin.socialmediaapi.storage.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.timutkin.socialmediaapi.storage.entity.PostEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
    PostEntity findByAuthorId(@NonNull Long id);

    @Query(value = "SELECT p FROM PostEntity p  JOIN FETCH p.author left JOIN FETCH p.images order by p.created DESC ")
    List<PostEntity> findAllFetch();

    @Query(value = "SELECT p FROM PostEntity p JOIN FETCH p.author left JOIN FETCH p.images " +
                   "WHERE p.author.id = :userId order by p.created DESC ")
    List<PostEntity> findAllByIdFetch(@Param("userId") Long userId);

    @EntityGraph(value = "post-with-images", type = EntityGraph.EntityGraphType.FETCH)
    Optional<PostEntity> findById(Long id);

    boolean existsByAuthorIdAndId(Long authorId, Long postId);

    @Query(value = """
           SELECT p
           FROM PostEntity p JOIN p.author left JOIN p.images
           JOIN SubscribeEntity s ON s.subscriberToId = p.author.id WHERE  s.subscriberId = :userId""")
    List<PostEntity> findBySubscribe(@Param("userId") Long userId, Pageable pageable);


}