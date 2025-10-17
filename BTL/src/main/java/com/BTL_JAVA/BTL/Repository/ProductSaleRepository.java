package com.BTL_JAVA.BTL.Repository;

import com.BTL_JAVA.BTL.Entity.Product.ProductSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSaleRepository extends JpaRepository<ProductSale, Integer> {
    List<ProductSale> findBySaleId(Integer saleId);
}