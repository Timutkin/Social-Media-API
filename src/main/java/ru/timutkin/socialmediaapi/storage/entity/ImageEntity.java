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
public class ImageEntity {

    @Id
    @SequenceGenerator(name = "image_gen", sequenceName = "image_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_gen")
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    @Lob
    private byte[] image;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;
}