package ru.timutkin.socialmediaapi.storage.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.timutkin.socialmediaapi.storage.enumeration.Role;


@Getter
@Setter
@NoArgsConstructor
@Table(name = "role", schema = "public")
@Entity
public class RoleEntity extends BaseEntity{

    @Id
    @SequenceGenerator(name = "role_gen", sequenceName = "role_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_gen")
    private Integer id;

    @Enumerated(EnumType.STRING)
    private Role role;

}
