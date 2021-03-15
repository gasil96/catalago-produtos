# Catálago de Produtos

Aplicação feita com intuito de demonstrar algumas funcionalidades em desenvolvimento de API Rest com **Spring**.

# Arquitetura MVC

Utilizado padrão **MVC ( Model-View-Controller )**, separando classes de domínio, dtos de auxilio, enums, etc... (referente a negócio) no pacote domain, classes responsavéis por executar nossa regra de négocio no pacote de serviço (service) usando interfaces para diminuir a complexibilidade e aumentando a manutenibilidade do sistema. O pacote de controller fica responsável por servir  os endpoints para acesso ao recurso.

<arquite-mvc.png>

## Create files and folders

The file explorer is accessible using the button in left corner of the navigation bar. You can create a new file by clicking the **New file** button in the file explorer. You can also create folders by clicking the **New folder** button.

## Crud Genérico

No pacote de serviço foi adotado uma interface genérica para otimizar o processo de construção de CRUDS, garantindo que a implementação de serviço de todas entidades possuam os mesmos métodos padrões.

##### Classe CRUD Genérica
``` Java 
public interface ICrudService<Z> {  
  
  Z save(Z genericClass);  
  
  Z update(Z genericClass, Long id) throws Exception;  
  
  Z findById(Long id);  
  
  List<Z> findAll();  
  
  void deleById(Long id);  
  
}
```
##### Classe CRUD Individual

``` Java
public interface IProductService extends ICrudService<Product> {  
  
    List<Product> search(BigDecimal minPrice, BigDecimal maxPrice, String q);  
  
}
```
##### Classe de Implementação

``` java
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
  
      return productRepository.searchFiltered(q, q, minPrice, maxPrice);  
  }  
   
}
```

## Banco de Dados H2

Na aplicação é utilizado o banco de dados em memória **H2** para  armazenar os dados em tempo de execução.

``` yml
spring:  
  datasource:  
    url: jdbc:h2:mem:runtimedb  
    driver-class-name: org.h2.Driver  
    username: sa  
    password:  
  h2:  
    console:  
      enabled: true  
      path: /h2  
  jpa:  
    show-sql: true
```

Dentro da pasta **resource** foi colocado um aquivo **data.sql** com um script para popular o banco com alguns registros sempre que o projeto for iniciado.

``` sql
INSERT INTO PRODUCT(ID_PRODUCT, NAME, DESCRIPTION, PRICE, DATE_CREATED, HOTSTNAME_CREATED_IP, DATE_CHANGED,  
  HOTSTNAME_CHANGED_IP)  
VALUES (9991, 'Kaik Urbe', 'Kaik', 94.90, '2020-04-23', 'ROBSON-PAULO-KRAMER-PC', NULL, NULL),  
  (9992, 'Essencial Mirra', 'Essencial Mirra Masculino', 129.90, '2020-04-23', 'LENOVO-GAMMING3I', NULL, NULL),  
  (9993, 'Luna @Absoluta', 'Luna', 129.90, '2020-04-23', 'LENOVO-GAMMING3I', NULL, NULL),  
  (9994, 'Humor', 'Meu primeiro Humor', 109.90, '2020-04-23', 'WINDOWS-SERVER-2008', NULL, NULL),  
  (9995, 'Biografia', 'Biografia Masculino', 94.90, '2020-04-23', 'LENOVO-GAMMING3I', NULL, NULL),  
  (9996, 'SR N', 'Sr N', 96.90, '2020-04-23', 'UBUNTU-SERVER', NULL, NULL),  
  (9997, 'Sintonia', 'Humor Sintonia clássico', 134.90, '2020-04-23', 'LENOVO-GAMMING3I', NULL, NULL),  
  (9998, 'Kriska Shock', 'Kriska', 300, '2020-04-23', 'LENOVO-GAMMING3I', NULL, NULL),  
  (9999, 'Hoje', '@Desodorante Colonia', 87.90, '2020-04-23', 'LENOVO-GAMMING3I', '2020-04-24', 'UBUNTU-SERVE'),  
  (99910, 'Una Blush', 'Una', 167.20, '2020-04-23', 'LENOVO-GAMMING3I', NULL, NULL);
```
Esses mesmos registros são utilizados nos testes unitários dos endpoints


##   Exception Handler

Algumas *exceptions* foram tratadas na aplicação para retornar um response de erro padronizado juntamente com as exceções customizadas criadas para a aplicação.

``` java
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseBody
    @ExceptionHandler(ProducNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected void produtctNotFoundHandler() {
        /*
          No body response
          */
    }

    @ResponseBody
    @ExceptionHandler(UnssuportedValueMinMaxException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    protected ApiError unssuporteValueMinMaxException(UnssuportedValueMinMaxException ex) {
        return new ApiError(409, ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    protected ApiError unssuporteValueMinMaxException(NumberFormatException ex) {
        return new ApiError(409, ex.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> details = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }
        return new ResponseEntity<>(new ApiError(400, details.toString()), HttpStatus.BAD_REQUEST);
    }

}
```

``` Java
public class UnssuportedValueMinMaxException extends RuntimeException {  
  
    public UnssuportedValueMinMaxException(BigDecimal minValue, BigDecimal maxValue) {  
        super("The minimum value(" + minValue + ") cannot be greater than the maximum value (" + maxValue + ")");  
  }  
  
}
```


## Classe de Auditoria

Uma classe de auditoria simples foi feita para ser aplicada em todas nossas entidades de domínio, gerando uma data para criação e edição de registros e host do usuário em questão.

``` java
@MappedSuperclass  
public abstract class Audit {  
  
    @Column(name = "DATE_CREATED")  
    private LocalDateTime created;  
  
    @Column(name = "DATE_CHANGED")  
    private LocalDateTime changed;  
  
    @Column(name = "HOTSTNAME_CREATED_IP")  
    private String userCreatedHostname;  
  
    @Column(name = "HOTSTNAME_CHANGED_IP")  
    private String userChangedHostname;  
  
    @PrePersist  
    public void prePersist() throws UnknownHostException {  
        userCreatedHostname = InetAddress.getLocalHost().getHostName();  
        created = LocalDateTime.now();  
    }  
  
    @PreUpdate  
    public void preUpdate() throws UnknownHostException {  
        userChangedHostname = InetAddress.getLocalHost().getHostName();  
        changed = LocalDateTime.now();  
    }  
  
    public LocalDateTime getCreated() {  
        return created;  
    }  
    
    public void setCreated(LocalDateTime created) {  
        this.created = created;  
    }  
    
    public LocalDateTime getChanged() {  
        return changed;  
    }  
    
    public void setChanged(LocalDateTime changed) {  
        this.changed = changed;  
    }  
    
    public String getUserCreatedHostname() {  
        return userCreatedHostname;  
    }  
    
    public void setUserCreatedHostname(String userCreatedHostname) {  
        this.userCreatedHostname = userCreatedHostname;  
    }  
    
    public String getUserChangedHostname() {  
        return userChangedHostname;  
    }  
    
    public void setUserChangedHostname(String userChangedHostname) {  
        this.userChangedHostname = userChangedHostname;  
    }  
}
```

Exemplo de uso...

``` java
@Entity  
@Table(name = "PRODUCT")  
public class Product extends Audit {  
  
    @Id  
    @GeneratedValue(strategy = GenerationType.SEQUENCE)  
    @Column(name = "ID_PRODUCT")  
    @NotNull  
    private Long id;  
  
    @Column(name = "NAME")  
    @NotNull(message = "name is required")  
    private String name;  
  
    @Column(name = "DESCRIPTION")  
    @NotNull(message = "description is required")  
    private String description;  
  
    @Column(name = "PRICE")  
    @NotNull(message = "price is required")  
    @Min(value = 0, message = "Price cannot be negative")  
    private BigDecimal price;  
  
    public Long getId() {  
        return id;  
    }  
    
    public void setId(Long id) {  
        this.id = id;  
    }  
    
    public String getName() {  
        return name;  
    }  
    
    public void setName(String name) {  
        this.name = name;  
    }  
    
    public String getDescription() {  
        return description;  
    }  
    
    public void setDescription(String description) {  
        this.description = description;  
    }  
    
    public BigDecimal getPrice() {  
        return price;  
    }  
    
    public void setPrice(BigDecimal price) {  
        this.price = price;  
    }  
}
```


# Model Mapper e DTO

Para transportar os dados entre banco e cliente foi utilizado DTO  garantindo a segurança dos dados e tratamento do mesmo. O ModelMapper foi o framework escolhido para fazer o convert dos dados.

``` java
// ... alguns metodos foram omitidos
  

    @Autowired
    private IProductService iProductService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("products")
    @Operation(summary = "creating a product")
    public ResponseEntity<ProductDTO> save(@Valid @RequestBody ProductDTO productDto) {
        return new ResponseEntity<>(this.convertToDto(iProductService
                .save(this.convertToEntity(productDto))), HttpStatus.CREATED);
    }

    private ProductDTO convertToDto(Product product) {
        return modelMapper.map(product, ProductDTO.class);
    }

    private Product convertToEntity(ProductDTO productDTO) {
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
``` 
Exemplo de DTO

``` java
@Data // Lombok Annotation
public class ProductDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // View only Return
    private String id;

    @NotBlank(message = "Name is required and not empty")
    private String name;

    @NotBlank(message = "Name is required and not empty")
    private String description;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "price cannot be negative")
    private BigDecimal price;

}

```
No DTO foi utilizado [LOMBOK](https://projectlombok.org/) para evitar uso de *getters* e *setters*

## Open API - Swagger

Para documentação da API foi utilizado o **Open API Swagger** do *SpringDocs*. Após iniciar a aplicação
em sua raiz "/" você sera redirecinado para o *swagger-ui.html*  com toda documentação necessária para entender os recursos, de nome e versão da api até descrição de cada barramento.

<swagger-1.png>

Também é possível  testar os endpoints pelo próprio swagger

**POST** /products
<swagger-2.png>

**GET** /products/search
<swagger-3.png>


## Testes Unitários

Uma boa prática pra desenvolvimento de software é realizar testes unitários nesta API foi feito alguns testes simples nos retornos da camada controlladora.

``` java
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
```
Resultados...
<teste.png>

## Rodando à aplicação
A aplicação está setada para subir na porta 9999, após baixar o projeto entre no diretório e rode.

``` bash
mvn spring-boot:run
```

Lembrando que o java que foi utilizado esta na versão 11.

