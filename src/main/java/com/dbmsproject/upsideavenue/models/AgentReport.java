package com.dbmsproject.upsideavenue.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentReport {
    private String username;
    private String accountname;
    private long transactions;
    private double avgSales;
    private double duration;
}
