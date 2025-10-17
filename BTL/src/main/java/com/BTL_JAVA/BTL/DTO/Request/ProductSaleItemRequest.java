package com.BTL_JAVA.BTL.DTO.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductSaleItemRequest {
    Integer productId;
    BigDecimal value;
}
