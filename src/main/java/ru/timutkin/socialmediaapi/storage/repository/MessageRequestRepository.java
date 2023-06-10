package ru.timutkin.socialmediaapi.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.timutkin.socialmediaapi.storage.entity.MessageRequestEntity;

public interface MessageRequestRepository extends JpaRepository<MessageRequestEntity, Long> {

    boolean existsByReceiverIdAndSenderId(Long fromId, Long toId);
}