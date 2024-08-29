package ru.skypro.homework.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "ads")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ad extends ParentIdEntity {

    private String title;

    private Integer price;

    private String description;
    @ManyToMany

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image image;

    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ad_id", referencedColumnName = "id")
    private List<Comment> comments;

    @Builder
    public Ad(long id, String title, int price, String description, Image image, User user, List<Comment> comments) {
        super(id);
        this.title = title;
        this.price = price;
        this.description = description;
        this.image = image;
        this.user = user;
        this.comments = comments;
    }
}
