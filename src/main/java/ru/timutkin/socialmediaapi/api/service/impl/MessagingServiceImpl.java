package ru.timutkin.socialmediaapi.api.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.timutkin.socialmediaapi.api.exception.MessageRequestAlreadyExistsException;
import ru.timutkin.socialmediaapi.api.exception.UserNotFoundException;
import ru.timutkin.socialmediaapi.api.service.MessagingService;
import ru.timutkin.socialmediaapi.storage.entity.MessageRequestEntity;
import ru.timutkin.socialmediaapi.storage.enumeration.MessageRequestStatus;
import ru.timutkin.socialmediaapi.storage.repository.MessageRequestRepository;
import ru.timutkin.socialmediaapi.storage.repository.UserRepository;

@AllArgsConstructor
@Service
public class MessagingServiceImpl implements MessagingService {

    private final MessageRequestRepository messageRequestRepository;

    private final UserRepository userRepository;
    @Override
    @Transactional
    public void sendRequestToMessaging(Long toUserId, Long fromUserId) {
        if (!userRepository.existsById(toUserId)){
            throw new UserNotFoundException("User with id = " + toUserId + " not found");
        }
        if (messageRequestRepository.existsByReceiverIdAndSenderId(toUserId, fromUserId)){
            throw new MessageRequestAlreadyExistsException("User message request to user with id = " + toUserId + " already exists");
        }
        messageRequestRepository.save(
                MessageRequestEntity.builder()
                        .senderId(fromUserId)
                        .receiverId(toUserId)
                        .messageRequestStatus(MessageRequestStatus.PENDING)
                        .build()
        );
    }
}
