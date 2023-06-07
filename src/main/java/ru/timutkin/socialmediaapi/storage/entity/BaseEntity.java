package ru.timutkin.socialmediaapi.storage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import ru.timutkin.socialmediaapi.storage.enumeration.Status;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity {

    @CreatedDate
    @Column(name = "created")
    private Date created = new Date();

    @LastModifiedDate
    @Column(name = "updated")
    private Date updated = new Date();

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.ACTIVE;
}
