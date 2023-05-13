package com.dbmsproject.upsideavenue.repositories;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dbmsproject.upsideavenue.models.Furnished;
import com.dbmsproject.upsideavenue.models.Mode;
import com.dbmsproject.upsideavenue.models.Post;

public interface PostRepository extends JpaRepository<Post, UUID> {

        @Query("""
                        select p from Post p inner join Property pr\s
                        on p.propertyId=pr.propertyId\s
                        where pr.owner.username = :owner\s
                        and p.postStatus = PostStatus.AVAILABLE\s
                        """)
        List<Post> findAllPostByOwner(String owner);

        @Query("""
                        select p from Post p inner join Property pr\s
                        on p.propertyId=pr.propertyId\s
                        where pr.owner.username != :owner\s
                        and p.postStatus = PostStatus.AVAILABLE\s
                        """)
        List<Post> findAllPostNotOwned(String owner);

        @Query("""
                        select p from Post p inner join Property pr\s
                        on p.propertyId=pr.propertyId\s
                        where pr.owner.username != :owner\s
                        and (:city is null or :city = '' or lower(pr.city) like lower(concat('%',:city,'%')))\s
                        and (:state is null or :state = '' or lower(pr.state) like lower(concat('%',:state,'%')))\s
                        and (:minPrice is null or p.price >= :minPrice)\s
                        and (:maxPrice is null or p.price <= :maxPrice)\s
                        and (:mode is null or p.mode = :mode)\s
                        and (:minSize is null or pr.propertySize >= :minSize)\s
                        and (:maxSize is null or pr.propertySize <= :maxSize)\s
                        and (:furnished is null or pr.furnished = :furnished)\s
                        and (:minBedrooms is null or pr.bedrooms >= :minBedrooms)\s
                        and (:maxBedrooms is null or pr.bedrooms <= :maxBedrooms)\s
                        and (:minDate is null or pr.constructionDate >= :minDate)\s
                        and (:maxDate is null or pr.constructionDate <= :maxDate)\s
                        and p.postStatus = PostStatus.AVAILABLE\s
                        """)
        List<Post> filter(
                        String owner,
                        String city,
                        String state,
                        Double minPrice,
                        Double maxPrice,
                        Mode mode,
                        Integer minSize,
                        Integer maxSize,
                        Furnished furnished,
                        Integer minBedrooms,
                        Integer maxBedrooms,
                        Date minDate,
                        Date maxDate);

        @Query("""
                        select p from Post p\s
                        where p.agentId.username = :agent\s
                        and p.postStatus = PostStatus.AVAILABLE\s
                        """)
        List<Post> findAllPostByAgent(String agent);

}