package com.BTL_JAVA.BTL.DTO.Response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryResponse {
        Integer categoryId;

        String categoryName;

        Integer perentId;

        String image;

        Integer   productCount;

      Set<Integer> productIds;
}
