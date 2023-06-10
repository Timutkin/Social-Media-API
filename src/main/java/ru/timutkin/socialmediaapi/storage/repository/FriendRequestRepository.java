package ru.timutkin.socialmediaapi.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.timutkin.socialmediaapi.storage.entity.FriendRequestEntity;
import ru.timutkin.socialmediaapi.storage.enumeration.FriendRequestStatus;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequestEntity, Long> {

    @Query(value = "DELETE FROM FriendRequestEntity fr WHERE fr.senderId =:fromId " +
                   "and fr.receiverId = :toId")
    @Modifying(clearAutomatically = true)
    void deleteByReceiverIdAndSenderId(Long fromId, Long toId);

    boolean existsByReceiverIdAndSenderId(Long fromId, Long toId);

    List<FriendRequestEntity> findByReceiverId(Long id);

    @Modifying
    @Query(value = "UPDATE FriendRequestEntity fr SET fr.friendRequestStatus = :requestStatus " +
                   "WHERE fr.receiverId = :toId AND  fr.senderId = :fromId")
    void setStatusRequestByReceiverIdAndSenderId(@Param("toId") Long toId,
                                                 @Param("fromId") Long fromId,
                                                 @Param("requestStatus") FriendRequestStatus friendRequestStatus);


}