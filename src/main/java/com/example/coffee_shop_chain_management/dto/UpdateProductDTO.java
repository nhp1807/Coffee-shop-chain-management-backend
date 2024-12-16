package com.example.coffee_shop_chain_management.dto;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProductDTO {
    String name;
    String description;
    Double price;
    String image;
    String recipe;
}