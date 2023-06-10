package ru.timutkin.socialmediaapi.storage.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    private List<ImageEntity> images = new ArrayList<>();

    public void addImageEntity(ImageEntity image) {
        images.add(image);
        if (image.getPost() != this){
            image.setPost(this);
        }
    }

}
