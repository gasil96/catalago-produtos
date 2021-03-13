package br.com.catalagoproduto.catalagoprotudo.manager.impl;

import br.com.catalagoproduto.catalagoprotudo.domain.model.Product;
import br.com.catalagoproduto.catalagoprotudo.manager.interfaces.IProductService;
import br.com.catalagoproduto.catalagoprotudo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product save(Product genericClass) {
        return productRepository.save(genericClass);
    }

    @Override
    public Product update(Product genericClass, Long id) {
        //TODO - Implementar busca
        return null;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public void deleById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> search(String q, BigDecimal minPrice, BigDecimal maxPrice) {
        //TODO - Implementar lógica de busca
        return null;
    }
}
