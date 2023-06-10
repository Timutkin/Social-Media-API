package ru.timutkin.socialmediaapi.storage.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "subscribe")
public class SubscribeEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subscribe_entity_seq")
    @SequenceGenerator(name = "subscribe_entity_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "subscriber_id", insertable = false, updatable = false)
    private UserEntity subscriber;

    @Column(name = "subscriber_id")
    private Long subscriberId;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "subscribed_to_id", insertable = false, updatable = false)
    private UserEntity subscriberTo;

    @Column(name = "subscribed_to_id")
    private Long subscriberToId;

}