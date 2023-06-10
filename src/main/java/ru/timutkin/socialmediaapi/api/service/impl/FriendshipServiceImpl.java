package ru.timutkin.socialmediaapi.api.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.timutkin.socialmediaapi.api.dto.FriendRequestDto;
import ru.timutkin.socialmediaapi.api.exception.FriendRequestAlreadyExistsException;
import ru.timutkin.socialmediaapi.api.exception.FriendRequestNotFoundException;
import ru.timutkin.socialmediaapi.api.exception.UserNotFoundException;
import ru.timutkin.socialmediaapi.api.mapper.FriendRequestMapper;
import ru.timutkin.socialmediaapi.api.service.FriendshipService;
import ru.timutkin.socialmediaapi.storage.entity.FriendRequestEntity;
import ru.timutkin.socialmediaapi.storage.entity.FriendshipEntity;
import ru.timutkin.socialmediaapi.storage.entity.SubscribeEntity;
import ru.timutkin.socialmediaapi.storage.enumeration.FriendRequestStatus;
import ru.timutkin.socialmediaapi.storage.repository.FriendRequestRepository;
import ru.timutkin.socialmediaapi.storage.repository.FriendshipRepository;
import ru.timutkin.socialmediaapi.storage.repository.SubscribeRepository;
import ru.timutkin.socialmediaapi.storage.repository.UserRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class FriendshipServiceImpl implements FriendshipService {

    private final FriendRequestRepository friendRequestRepository;

    private final SubscribeRepository subscribeRepository;

    private final UserRepository userRepository;

    private final FriendRequestMapper friendRequestMapper;

    private final FriendshipRepository friendshipRepository;

    @Override
    @Transactional
    public void sendRequestToFriendShip(Long toId, Long fromId) {
        if (!userRepository.existsById(toId)){
            throw new UserNotFoundException("User with id = " + toId + " not found");
        }
        if (toId.equals(fromId)){
            throw new FriendRequestAlreadyExistsException("You can't send a friend request to yourself");
        }
        if (friendRequestRepository.existsByReceiverIdAndSenderId(toId, fromId)){
            throw new FriendRequestNotFoundException("User friend request to user with id = " + toId + " already exists" );
        }
        friendRequestRepository.save(
                FriendRequestEntity.builder()
                        .senderId(fromId)
                        .receiverId(toId)
                        .friendRequestStatus(FriendRequestStatus.PENDING)
                        .build()
        );
        subscribeRepository.save(
                SubscribeEntity.builder()
                        .subscriberId(fromId)
                        .subscriberToId(toId)
                        .build()
        );
    }

    @Override
    @Transactional
    public void removeRequestToFriendShip(Long toId, Long fromId) {
        if (!userRepository.existsById(toId)){
            throw new UserNotFoundException("User with id = " + toId + " not found");
        }
        if (friendRequestRepository.existsByReceiverIdAndSenderId(toId, fromId)){
            throw new FriendRequestNotFoundException("User request to user with id = " + toId + " not found" );
        }
        friendRequestRepository.deleteByReceiverIdAndSenderId(fromId, toId);
        subscribeRepository.deleteBySubscriberIdAndSubscriberToId(fromId, toId);
    }

    @Override
    public List<FriendRequestDto> getAllFriendRequests(Long id) {
        return friendRequestRepository.findByReceiverId(id).stream()
                .map(friendRequestMapper::friendRequestEntityToFriendRequestDto).toList();
    }

    @Override
    @Transactional
    public void receiveFriendRequest(Long fromUserId, Long toUserId) {
        if (!userRepository.existsById(toUserId)){
            throw new UserNotFoundException("User with id = " + toUserId + " not found");
        }
        if (!friendRequestRepository.existsByReceiverIdAndSenderId(toUserId, fromUserId)){
            throw new FriendRequestNotFoundException("User friend request to user with id = " + toUserId + " not found" );
        }
        if (subscribeRepository.existsBySubscriberIdAndSubscriberToId(toUserId, fromUserId)){
            throw new FriendRequestAlreadyExistsException("Have you already accepted a friend request");
        }
        friendRequestRepository.setStatusRequestByReceiverIdAndSenderId(toUserId, fromUserId, FriendRequestStatus.ACCEPTED);
        subscribeRepository.save(
                SubscribeEntity.builder()
                        .subscriberId(toUserId)
                        .subscriberToId(fromUserId)
                        .build()
        );

        friendshipRepository.save(
                FriendshipEntity.builder()
                        .friendOneId(fromUserId)
                        .friendTwoId(toUserId)
                        .build()
        );
    }

    @Override
    @Transactional
    public void rejectFriendRequest(Long fromUserId, Long toUserId) {
        if (!userRepository.existsById(toUserId)){
            throw new UserNotFoundException("User with id = " + toUserId + " not found");
        }
        if (!friendRequestRepository.existsByReceiverIdAndSenderId(toUserId, fromUserId)){
            throw new FriendRequestNotFoundException("User friend request to user with id = " + fromUserId + " not found" );
        }
        /*
        If the users have not been friends before, and the user rejects the request
         */
        friendRequestRepository.setStatusRequestByReceiverIdAndSenderId(toUserId, fromUserId, FriendRequestStatus.REJECTED);
        /*
        If the users were previously friends, then the user is removed from friends
         */
        if (subscribeRepository.existsBySubscriberIdAndSubscriberToId(toUserId, fromUserId)){
            subscribeRepository.deleteBySubscriberIdAndSubscriberToId(toUserId, fromUserId);
        }
        friendshipRepository.deleteByFriendOneIdAndFriendTwoId(fromUserId, toUserId);

    }

    @Override
    public List<Long> getAllFriends(Long id) {
        return friendshipRepository.getFriendsListByUserId(id);
    }

}
