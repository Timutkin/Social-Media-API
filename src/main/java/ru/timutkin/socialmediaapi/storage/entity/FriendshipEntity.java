package ru.timutkin.socialmediaapi.storage.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "frienddship", schema = "public")
@Entity
public class FriendshipEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "frienddship_gen")
    @SequenceGenerator(name = "frienddship_gen", sequenceName = "frienddship_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_one", insertable = false, updatable = false)
    private UserEntity friendOne;

    @Column(name = "friend_one")
    private Long friendOneId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_two", insertable = false, updatable = false)
    private UserEntity friendTwo;

    @Column(name = "friend_two")
    private Long friendTwoId;

}
