package com.utp.creacionesjoaquin.repository;

import com.utp.creacionesjoaquin.model.Cart;
import com.utp.creacionesjoaquin.model.CartItem;
import com.utp.creacionesjoaquin.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {
    List<CartItem> findByCart(Cart cart);
    List<CartItem> findByProduct(Product product);
    void deleteAllByCart(Cart cart);
}
