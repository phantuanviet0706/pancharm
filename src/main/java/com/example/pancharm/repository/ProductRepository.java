package com.example.pancharm.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.pancharm.entity.Products;

@Repository
public interface ProductRepository extends JpaRepository<Products, Integer>, JpaSpecificationExecutor<Products> {
    boolean existsBySlug(String slug);

    boolean existsBySlugAndSoftDeleted(String slug, short softDeleted);

    List<Products> findByIdIn(Collection<Integer> ids);

    @Modifying
    @Query(
            """
		UPDATE Products p
		SET p.quantity = p.quantity - :qty
		WHERE p.id = :productId
		AND p.quantity >= :qty
	""")
    int decreaseStockIfEnough(@Param("productId") Long productId, @Param("qty") int qty);
}
