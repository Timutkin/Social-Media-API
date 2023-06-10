
package ru.timutkin.socialmediaapi.storage.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.timutkin.socialmediaapi.storage.enumeration.MessageRequestStatus;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "message_request")
public class MessageRequestEntity extends  BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_request_entity_seq")
    @SequenceGenerator(name = "message_request_entity_seq")
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
    private MessageRequestStatus messageRequestStatus;

}