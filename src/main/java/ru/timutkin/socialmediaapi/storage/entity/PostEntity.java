package ru.timutkin.socialmediaapi.storage.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "post", schema = "public")
@Entity
@NamedEntityGraph(name = "post-with-images", attributeNodes = {
        @NamedAttributeNode(value = "author", subgraph = "author-subgraph"),
        @NamedAttributeNode(value = "images", subgraph = "image-id-subgraph"),
        @NamedAttributeNode("id"),
        @NamedAttributeNode("header"),
        @NamedAttributeNode("text")
},
        subgraphs = {
                @NamedSubgraph(
                        name = "author-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("id"),
                                @NamedAttributeNode("username")
                        }
                ),
                @NamedSubgraph(
                        name = "image-id-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("id"),
                        }
                ),
        }
)
public class PostEntity extends BaseEntity {
    @Id
    @SequenceGenerator(name = "post_gen", sequenceName = "post_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_gen")
    @Column(name = "id", nullable = false)
    private Long id;

    private String header;

    private String text;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity author;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY )
    private Set<ImageEntity> images = new HashSet<>();

    public void addImageEntity(ImageEntity image) {
        images.add(image);
        if (image.getPost() != this){
            image.setPost(this);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PostEntity post = (PostEntity) o;
        return getId() != null && Objects.equals(getId(), post.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
