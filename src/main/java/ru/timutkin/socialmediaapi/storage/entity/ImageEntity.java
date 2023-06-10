package ru.timutkin.socialmediaapi.storage.entity;

import jakarta.persistence.*;
import lombok.*;

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
}
