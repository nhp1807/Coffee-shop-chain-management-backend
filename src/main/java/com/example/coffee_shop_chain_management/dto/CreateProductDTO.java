package com.example.coffee_shop_chain_management.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateProductDTO {
    String name;
    String description;
    Double price;
    String image;
    String recipe;
    List<ProductMaterialDTO> productMaterials;
}
