package ru.timutkin.socialmediaapi.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.timutkin.socialmediaapi.storage.entity.FriendshipEntity;

public interface FriendshipRepository extends JpaRepository<FriendshipEntity, Long> {

    void deleteByFriendOneIdAndFriendTwoId(Long first, Long second);
}