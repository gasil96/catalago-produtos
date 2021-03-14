package br.com.catalagoproduto.catalagoprotudo.repository;

import br.com.catalagoproduto.catalagoprotudo.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByDescriptionContainingIgnoreCase(String description);

    List<Product> findByPriceGreaterThanEqual(BigDecimal minPrice);

    List<Product> findByPriceLessThanEqual(BigDecimal maxPrice);

    @Query("SELECT p FROM Product p WHERE " +
            "(:name IS NULL OR p.name LIKE %:name%) " +
            "OR (:description IS NULL OR p.description LIKE %:description%) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice)")
    List<Product> searchFiltered(@Param("name") String name,
                                 @Param("description") String description,
                                 @Param("minPrice") BigDecimal minPrice,
                                 @Param("maxPrice") BigDecimal maxPrice);

}
