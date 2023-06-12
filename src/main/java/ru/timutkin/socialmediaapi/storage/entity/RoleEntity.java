package ru.timutkin.socialmediaapi.storage.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import ru.timutkin.socialmediaapi.storage.enumeration.Role;

import java.util.Objects;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RoleEntity that = (RoleEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
