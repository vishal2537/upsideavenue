package com.dbmsproject.upsideavenue.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dbmsproject.upsideavenue.models.PurchaseRequest;

public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequest, UUID> {

    @Query("""
            select pr from PurchaseRequest pr\s
            where pr.post.postId = :postId\s
            and pr.buyer.username = :username\s
            """)
    Optional<PurchaseRequest> findPurchaseRequestByPostAndBuyer(UUID postId, String username);

    @Query("""
            select pr from PurchaseRequest pr\s
            where pr.buyer.username = :username\s
            """)
    List<PurchaseRequest> findAllPurchaseRequestByBuyer(String username);

    @Query("""
            select pr from PurchaseRequest pr\s
            where pr.post.postId = :postId\s
            """)
    List<PurchaseRequest> findAllPurchaseRequestByPost(UUID postId);

}
