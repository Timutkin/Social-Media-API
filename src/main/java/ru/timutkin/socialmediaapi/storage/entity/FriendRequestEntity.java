package ru.timutkin.socialmediaapi.storage.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.timutkin.socialmediaapi.storage.enumeration.FriendRequestStatus;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "friend_request", schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "sender_id"),
                @UniqueConstraint(columnNames = "receiver_id")
        }
)
@Entity
public class FriendRequestEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "friend_request_gen")
    @SequenceGenerator(name = "friend_request_gen", sequenceName = "friend_request_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id",insertable = false, updatable = false)
    private UserEntity sender;

    @Column(name = "sender_id")
    private Long senderId;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id",insertable = false, updatable = false)
    private UserEntity receiver;

    @Column(name = "receiver_id")
    private Long receiverId;

    @Enumerated(EnumType.STRING)
    private FriendRequestStatus friendRequestStatus;

}
