package br.com.catalagoproduto.catalagoprotudo.controller;

import br.com.catalagoproduto.catalagoprotudo.domain.dto.ProductDTO;
import br.com.catalagoproduto.catalagoprotudo.domain.model.Product;
import br.com.catalagoproduto.catalagoprotudo.manager.interfaces.IProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@Tag(name = "Product", description = "Product management")
public class ProductController {

    Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private IProductService iProductService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("products")
    @Operation(summary = "creating a product")
    public ResponseEntity<ProductDTO> save(@Valid @RequestBody ProductDTO productDto) {
        log.info("Save request body -> {}", productDto);
        return new ResponseEntity<>(this.convertToDto(iProductService
                .save(this.convertToEntity(productDto))), HttpStatus.CREATED);
    }

    @PutMapping("products/{id}")
    @Operation(summary = "updating a product")
    public ResponseEntity<ProductDTO> update(@Valid @RequestBody ProductDTO productDto,
                                             @PathVariable("id") Long id) throws Exception {
        log.info("Update request body -> {} for product id: {}", productDto, id);
        return new ResponseEntity<>(this.convertToDto(iProductService
                .update(this.convertToEntity(productDto), id)), HttpStatus.OK);
    }

    @GetMapping("products/{id}")
    @Operation(summary = "searching for a product by ID")
    public ResponseEntity<ProductDTO> findById(@PathVariable("id") Long id) {
        log.info("find by product with id: {}", id);
        return new ResponseEntity<>(this.convertToDto(iProductService.findById(id)), HttpStatus.OK);
    }

    @GetMapping("products")
    @Operation(summary = "product list")
    public ResponseEntity<List<ProductDTO>> findAll() {
        log.info("find by all products in database...");
        return new ResponseEntity<>(iProductService.findAll().stream()
                .map(this::convertToDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("products/search")
    @Operation(summary = "list of filtered products")
    public ResponseEntity<List<ProductDTO>> search(@RequestParam(required = false) BigDecimal min_price,
                                                   @RequestParam(required = false) BigDecimal max_price,
                                                   @RequestParam(required = false) String q) {
        log.info("find by all products in database with filters: minPrice: {}, maxPrice: {}, q: {}",
                min_price, max_price, q);
        return new ResponseEntity<>(iProductService.search(min_price, max_price, q).stream()
                .map(this::convertToDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @DeleteMapping("products/{id}")
    @Operation(summary = "deletion of a product")
    public void deleteById(@PathVariable("id") Long id) {
        log.info("delete product with id: {}", id);
        iProductService.deleById(id);
    }

    private ProductDTO convertToDto(Product product) {
        log.info("Convert entity -> {} in DTO", product.toString());
        return modelMapper.map(product, ProductDTO.class);
    }

    private Product convertToEntity(ProductDTO productDTO) {
        log.info("converting DTO -> {} in entity", productDTO);
        ModelMapper customMapper = new ModelMapper();
        customMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
        customMapper.addMappings(new PropertyMap<ProductDTO, Product>() {
            @Override
            protected void configure() {
                if (source.getId() != null)
                    map().setId(Long.valueOf(source.getId()));
            }
        });
        return customMapper.map(productDTO, Product.class);
    }


}
