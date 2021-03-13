package br.com.catalagoproduto.catalagoprotudo.manager.interfaces;

import br.com.catalagoproduto.catalagoprotudo.domain.model.Product;

import java.math.BigDecimal;
import java.util.List;

public interface IProductService extends ICrudService<Product> {

    List<Product> search(String q, BigDecimal minPrice, BigDecimal maxPrice);

}
