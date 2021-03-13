package br.com.catalagoproduto.catalagoprotudo.repository;

import br.com.catalagoproduto.catalagoprotudo.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
