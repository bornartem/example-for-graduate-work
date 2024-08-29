package ru.skypro.homework.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "images")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Image extends ParentIdEntity {

    private long fileSize;

    private String mediaType;

    private byte[] data;

    @Builder
    public Image(long id, long fileSize, String mediaType, byte[] data) {
        super(id);
        this.fileSize = fileSize;
        this.mediaType = mediaType;
        this.data = data;
    }
}
