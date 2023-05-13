package com.dbmsproject.upsideavenue.models;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "properties")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID propertyId;

    @Column(nullable = false)
    private String propertyName;

    @Column(nullable = false)
    @Default
    private Date constructionDate = new Date(System.currentTimeMillis());

    @ManyToOne
    @JoinColumn(name = "owner", nullable = false)
    private User owner;

    private String houseNumber;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false, length = 8)
    private Integer zipCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PropertyType propertyType;

    @Column(nullable = false)
    private Integer propertySize;

    @Column(nullable = false)
    private Integer bedrooms;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Furnished furnished;

    @OneToMany(mappedBy = "propertyId")
    private List<Photo> photos;

    @OneToOne(mappedBy = "property")
    private Rent rent;

    public boolean isRent() {
        return rent != null;
    }

}
