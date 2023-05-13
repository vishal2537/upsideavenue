package com.dbmsproject.upsideavenue.models;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesReport {
    private Date saleDate;
    private Property property;
    private String seller;
    private String buyer;
    private Mode mode;
    private double price;
}
