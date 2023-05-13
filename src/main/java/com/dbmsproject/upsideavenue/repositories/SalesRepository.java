package com.dbmsproject.upsideavenue.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dbmsproject.upsideavenue.models.AgentReport;
import com.dbmsproject.upsideavenue.models.Sales;
import com.dbmsproject.upsideavenue.models.SalesReport;
import com.dbmsproject.upsideavenue.models.primaryIds;

public interface SalesRepository extends JpaRepository<Sales, primaryIds> {
    @Query("""
            select new com.dbmsproject.upsideavenue.models.AgentReport(u.username, u.accountname, count(u.username), avg(p.price),  AVG(FUNCTION('DateDiff', s.saleDate, p.postDate)))\s
            from User u inner join Post p on u.username = p.agentId.username\s
            inner join Sales s on s.allId.postId.postId = p.postId\s
            group by p.agentId.username\s
            """)
    List<AgentReport> findAllAgentReport();

    @Query("""
            select new com.dbmsproject.upsideavenue.models.SalesReport(s.saleDate, p.propertyId, s.allId.sellerId.username, s.allId.buyerId.username, p.mode, p.price)\s
            from User u inner join Post p on u.username = p.agentId.username\s
            inner join Sales s on s.allId.postId.postId = p.postId\s
            where p.agentId.username=:username\s
            order by s.saleDate desc\s
            """)
    List<SalesReport> findAllSalesReportByAgent(String username);

}
