package com.alvaro.msvc.paymment.repositories;

import com.alvaro.msvc.paymment.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {

    @Query("""
        select c from Cart c
                where c.state = false and c.userId = :userId
        """)
    Optional<Cart> findByUserIdAndStateIsFalse(@Param("userId") UUID userId);

}
