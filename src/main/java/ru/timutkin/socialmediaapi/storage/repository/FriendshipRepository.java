package ru.timutkin.socialmediaapi.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.timutkin.socialmediaapi.storage.entity.FriendshipEntity;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<FriendshipEntity, Long> {

    void deleteByFriendOneIdAndFriendTwoId(Long first, Long second);

    @Query(value = """
            SELECT CASE
                    WHEN friend_one = :userId THEN friend_two
                    ELSE friend_one
                    END
            FROM friendship
            WHERE friend_one = :userId OR friend_two = :userId
            """,
            nativeQuery = true)
    List<Long> getFriendsListByUserId(@Param("userId") Long userId);
}