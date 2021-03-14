package br.com.catalagoproduto.catalagoprotudo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootTest(classes = CatalagoprodutoApplication.class)
@EnableJpaRepositories
@AutoConfigureMockMvc
class CatalagoprodutoApplicationTests {


}
