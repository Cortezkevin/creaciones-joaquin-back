package com.utp.creacionesjoaquin.sales.repository;

import com.utp.creacionesjoaquin.sales.model.Cart;
import com.utp.creacionesjoaquin.sales.model.CartItem;
import com.utp.creacionesjoaquin.administration.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {
    List<CartItem> findByCart(Cart cart);
    List<CartItem> findByProduct(Product product);
    void deleteAllByCart(Cart cart);
}
