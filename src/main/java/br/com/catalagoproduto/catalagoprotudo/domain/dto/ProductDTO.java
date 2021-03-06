package br.com.catalagoproduto.catalagoprotudo.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data // Lombok Annotation
@Schema(name = "Product")
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
