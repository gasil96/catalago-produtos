package br.com.catalagoproduto.catalagoprotudo.manager.impl;

import br.com.catalagoproduto.catalagoprotudo.configuration.exceptions.ProducNotFoundException;
import br.com.catalagoproduto.catalagoprotudo.configuration.exceptions.UnssuportedValueMinMaxException;
import br.com.catalagoproduto.catalagoprotudo.domain.model.Product;
import br.com.catalagoproduto.catalagoprotudo.manager.interfaces.IProductService;
import br.com.catalagoproduto.catalagoprotudo.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product save(Product genericClass) {
        return productRepository.save(genericClass);
    }

    @Override
    public Product update(Product genericClass, Long id) throws Exception {
        Product localizedProduct = this.findById(id);
        BeanUtils.copyProperties(genericClass, localizedProduct, "id");
        return productRepository.save(localizedProduct);
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new ProducNotFoundException(id));
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public void deleById(Long id) {
        this.findById(id);
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> search(BigDecimal minPrice, BigDecimal maxPrice, String q) {

        if (minPrice != null && maxPrice != null)
            if (minPrice.compareTo(maxPrice) > 0)
                throw new UnssuportedValueMinMaxException(minPrice, maxPrice);

        String name = q;

        String description = q;

        return productRepository.searchFiltered(name, description, minPrice, maxPrice);
    }


}
