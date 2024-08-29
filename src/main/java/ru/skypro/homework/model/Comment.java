package ru.skypro.homework.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends ParentIdEntity {

    @Column(name = "text")
    private String text;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Comment(long id, String text, LocalDateTime createdAt, User user) {
        super(id);
        this.text = text;
        this.createdAt = createdAt;
        this.user = user;
    }
}
