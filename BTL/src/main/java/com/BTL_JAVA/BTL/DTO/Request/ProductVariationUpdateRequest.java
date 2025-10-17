package com.BTL_JAVA.BTL.DTO.Request;


import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariationUpdateRequest {
    Integer productId;
    String  size;
    String  color;
    Integer stockQuantity;


    MultipartFile image;
}
