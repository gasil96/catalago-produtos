package br.com.catalagoproduto.catalagoprotudo.controller;

import br.com.catalagoproduto.catalagoprotudo.domain.model.Product;
import br.com.catalagoproduto.catalagoprotudo.manager.interfaces.IProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("product")
@Tag(name = "Product", description = "Product management")
public class ProductController {

    @Autowired
    private IProductService iProductService;

    @GetMapping("find-all")
    @Operation(summary = "")
    public ResponseEntity<List<Product>> findAll() {
        return new ResponseEntity<>(iProductService.findAll(), HttpStatus.OK);
    }


}
