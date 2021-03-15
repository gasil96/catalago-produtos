package br.com.catalagoproduto.catalagoprotudo.endpoints;

import br.com.catalagoproduto.catalagoprotudo.controller.ProductController;
import br.com.catalagoproduto.catalagoprotudo.domain.dto.ProductDTO;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductValidatorRestApplicationTests {

    @Autowired
    private ProductController productController;

    @Test
    @Order(6)
    void caseDeleteByIdSucess() {
        int results = Objects.requireNonNull(productController.findAll().getBody()).size();
        productController.deleteById(9991L);
        Assertions.assertEquals(Objects.requireNonNull(productController.findAll().getBody()).size(), results-1);

    }

    @Test
    @Order(1)
    void casePostSucess() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Name Product");
        productDTO.setDescription("Description Produtc");
        productDTO.setPrice(new BigDecimal(99.90));

        ResponseEntity<ProductDTO> result = productController.save((productDTO));
        Assertions.assertEquals(result.getStatusCode(), HttpStatus.CREATED);

        /**
         * Body return contain ID, but the send not
         * */
        productDTO.setId("1");
        Assertions.assertEquals(result.getBody(), productDTO);
    }

    @Test
    @Order(2)
    void casePutSucess() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Name Product");
        productDTO.setDescription("Description Produtc");
        productDTO.setPrice(new BigDecimal(99.90));
        ResponseEntity<ProductDTO> resultSaved = productController.save((productDTO));

        Long idReturnInObectSaved = Long.valueOf(Objects.requireNonNull(resultSaved.getBody()).getId());
        resultSaved.getBody().setId(null);
        resultSaved.getBody().setName("Name Product Altered");
        ResponseEntity<ProductDTO> resultAltered = productController.update(resultSaved.getBody(),
                idReturnInObectSaved);

        Assertions.assertEquals(resultAltered.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(resultAltered.getBody().getName(), "Name Product Altered");

    }

    @Test
    @Order(3)
    void caseFindByIdSucess() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Name Product");
        productDTO.setDescription("Description Produtc");
        productDTO.setPrice(new BigDecimal(99.90));

        ResponseEntity<ProductDTO> resultSaved = productController.save((productDTO));
        ResponseEntity<ProductDTO> result = productController
                .findById(Long.valueOf(Objects.requireNonNull(resultSaved.getBody()).getId()));

        Assertions.assertEquals(result.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(resultSaved.getBody().getId(), Objects.requireNonNull(result.getBody()).getId());
    }

    @Test
    @Order(4)
    void caseFindAllSucess() {
        ResponseEntity<List<ProductDTO>> results = productController.findAll();

        /**
         * File 'data.sql' in path resource contains 10 records + 2 add in before tests
         * */
        Assertions.assertEquals(results.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(Objects.requireNonNull(results.getBody()).size(), 11);

    }

    @Test
    @Order(5)
    void caseFindPerSearch() {
        ResponseEntity<List<ProductDTO>> results =
                productController.search(null, null, "@");
        /**
         * File 'data.sql' in path resource contains 2 records with "@" in name or description"
         * */

        Assertions.assertEquals(results.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(Objects.requireNonNull(results.getBody()).size(), 2);

        ResponseEntity<List<ProductDTO>> resulsWithParameter =
                productController.search(new BigDecimal(100), new BigDecimal(200), null);

        /**
         * Valid is paremeter min e max price
         * */
        Assertions.assertEquals(resulsWithParameter.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(resulsWithParameter.getBody().size(), 5);
    }


}
