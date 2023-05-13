package com.dbmsproject.upsideavenue.models;

import org.springframework.security.core.context.SecurityContextHolder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchPost {

    @Default
    private String owner = SecurityContextHolder.getContext().getAuthentication().getName();

    private String city;
    private String state;

    private Double minPrice;
    private Double maxPrice;

    private Mode mode;

    private Integer minSize;
    private Integer maxSize;

    private Furnished furnished;

    private Integer minBedrooms;
    private Integer maxBedrooms;

    private String minDate;
    private String maxDate;

}
