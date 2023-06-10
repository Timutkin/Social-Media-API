package ru.timutkin.socialmediaapi.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.timutkin.socialmediaapi.storage.entity.SubscribeEntity;

public interface SubscribeRepository extends JpaRepository<SubscribeEntity, Long> {


    @Query(value = "DELETE FROM SubscribeEntity sub WHERE sub.subscriberId =:fromId " +
                   "and sub.subscriberToId = :toId")
    @Modifying(clearAutomatically = true)
    void deleteBySubscriberIdAndSubscriberToId(@Param("fromId") Long fromId, @Param("toId") Long toId);

    boolean existsBySubscriberIdAndSubscriberToId(Long fromId, Long toId);
}