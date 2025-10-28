//JPA Entity for the `profile_photos` table.
package com.ksb.micro.profile_photo.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "profile_photos")
public class ProfilePhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //this column ensures that photo is tied to the bank
    @Column(nullable = false)
    private Long bankId;

    @Column(unique = true, nullable = false)
    private Long userId;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] photoData;
    private String contentType;
}
