package ru.timutkin.socialmediaapi.storage.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.timutkin.socialmediaapi.storage.enumeration.Role;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user",
        schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        }
)
@Entity
public class UserEntity extends BaseEntity {

    @Id
    @SequenceGenerator(name = "user_gen", sequenceName = "user_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_gen")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "author")
    private List<PostEntity> posts = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<RoleEntity> roles = new ArrayList<>();
}
