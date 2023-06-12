package ru.timutkin.socialmediaapi.storage.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "image", schema = "public")
@Entity
public class ImageEntity  extends BaseEntity{

    @Id
    @SequenceGenerator(name = "image_gen", sequenceName = "image_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_gen")
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    @Lob
    private byte[] image;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ImageEntity image = (ImageEntity) o;
        return getId() != null && Objects.equals(getId(), image.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
