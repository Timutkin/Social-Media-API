package ru.timutkin.socialmediaapi.storage.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.timutkin.socialmediaapi.storage.enumeration.Role;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "user", schema = "public")
@Entity
public class RoleEntity extends BaseEntity{

    @Id
    @SequenceGenerator(name = "role_gen", sequenceName = "role_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_gen")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<UserEntity> users = new ArrayList<>();
}
