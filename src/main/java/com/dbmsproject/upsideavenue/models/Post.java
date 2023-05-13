package com.dbmsproject.upsideavenue.models;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;

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
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID postId;

    @ManyToOne
    @JoinColumn(name = "agent_id", nullable = false)
    private User agentId;

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private Property propertyId;

    @Column(nullable = false)
    private Date postDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Mode mode;

    @Column(nullable = false)
    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Default
    private PostStatus postStatus = PostStatus.AVAILABLE;

    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "post")
    private List<PurchaseRequest> requests;

    public boolean isRent() {
        return mode == Mode.RENT;
    }

    public boolean filter(SearchPost search) {

        if (search.getCity() != null && !propertyId.getCity().toLowerCase().contains(search.getCity().toLowerCase()))
            return false;

        if (search.getState() != null && !propertyId.getState().toLowerCase().contains(search.getState().toLowerCase()))
            return false;

        if ((search.getMinPrice() != null && price < search.getMinPrice())
                || (search.getMaxPrice() != null && price > search.getMaxPrice()))
            return false;

        if (search.getMode() != null && mode != search.getMode())
            return false;

        if ((search.getMinSize() != null && propertyId.getPropertySize() < search.getMinSize())
                || (search.getMaxSize() != null && propertyId.getPropertySize() > search.getMaxSize()))
            return false;

        if (search.getFurnished() != null && propertyId.getFurnished() != search.getFurnished())
            return false;

        if ((search.getMinBedrooms() != null && propertyId.getBedrooms() < search.getMinBedrooms())
                || (search.getMaxBedrooms() != null && propertyId.getBedrooms() > search.getMaxBedrooms()))
            return false;

        return true;
    }

    public boolean purchaseAllowed() {
        return !propertyId.getOwner().getUsername()
                .equals(SecurityContextHolder.getContext().getAuthentication().getName());
    }

}
