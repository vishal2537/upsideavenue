
package com.dbmsproject.upsideavenue.models;

import java.util.Base64;
import java.util.UUID;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "photos")

public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID photoId;

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private Property propertyId;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "LONGBLOB", nullable = false)
    private byte[] photo;

    public String getPhotoData() {
        return Base64.getMimeEncoder().encodeToString(photo);
    }

}
