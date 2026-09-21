package com.alvaro.msvc.paymment.repositories;

import com.alvaro.msvc.paymment.models.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CartDetialRepository extends JpaRepository<CartDetail, UUID> {

    @Query("""
        select cd from CartDetail cd
        where cd.productId = :productId and cd.cart.cartId = :cartId
""")
    Optional<CartDetail> findByProductIdAndCartId(@Param("productId") UUID productId, @Param("cartId") UUID cartId);
}
