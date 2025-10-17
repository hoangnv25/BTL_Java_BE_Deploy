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
public class CategoryUpdateRequest {
    String categoryName;
    Integer perentId;
    MultipartFile image;

    Set<Integer> addProductIds;
    Set<Integer> removeProductIds;
}
