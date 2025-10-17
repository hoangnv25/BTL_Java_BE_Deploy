package com.BTL_JAVA.BTL.Service.Product;

import com.BTL_JAVA.BTL.DTO.Request.ApiResponse;
import com.BTL_JAVA.BTL.DTO.Request.ProductCreationRequest;
import com.BTL_JAVA.BTL.DTO.Request.ProductUpdateRequest;
import com.BTL_JAVA.BTL.DTO.Response.PageResult;
import com.BTL_JAVA.BTL.DTO.Response.ProductResponse;
import com.BTL_JAVA.BTL.DTO.Response.ProductVariationResponse;
import com.BTL_JAVA.BTL.Entity.Product.Category;
import com.BTL_JAVA.BTL.Entity.Product.Product;
import com.BTL_JAVA.BTL.Entity.Product.ProductVariation;
import com.BTL_JAVA.BTL.Exception.AppException;
import com.BTL_JAVA.BTL.Exception.ErrorCode;
import com.BTL_JAVA.BTL.Repository.CategoryRepository;
import com.BTL_JAVA.BTL.Repository.ProductRepository;
import com.BTL_JAVA.BTL.Repository.ProductVariationRepository;
import com.BTL_JAVA.BTL.Repository.Spec.ProductSpecs;
import com.BTL_JAVA.BTL.Service.Cloudinary.UploadImageFile;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import static org.springframework.data.jpa.domain.Specification.allOf;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {

    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    ProductVariationRepository productVariationRepository;
    UploadImageFile uploadImageFile;

    @Transactional
    public ApiResponse<ProductResponse> create(ProductCreationRequest req) throws IOException {
        Product p = Product.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .price(req.getPrice())
                .build();

        if (req.getCategoryId() != null) {
            Category cat = categoryRepository.findById(req.getCategoryId())
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
            p.setCategory(cat);
        }

        if (req.getImage() != null && !req.getImage().isEmpty()) {
            String url = uploadImageFile.uploadImage(req.getImage());
            p.setImage(url);
        }

        Product saved = productRepository.save(p);

        Set<Integer> vIds = req.getVariationIds() == null ? Set.of() : req.getVariationIds();
        if (!vIds.isEmpty()) {
            var variations = productVariationRepository.findAllById(vIds);
            var found = variations.stream().map(ProductVariation::getId).collect(Collectors.toSet());
            var missing = vIds.stream().filter(i -> !found.contains(i)).collect(Collectors.toSet());
            if (!missing.isEmpty()) {
                throw new AppException(ErrorCode.VARIATION_NOT_FOUND,"Variation không tồn tại: " + missing);
            }
            variations.forEach(v -> v.setProduct(saved));
            productVariationRepository.saveAll(variations);
        }

        return ApiResponse.<ProductResponse>builder()
                .code(1000).message("Success")
                .result(toResponse(saved))
                .build();
    }


    @Transactional
    public ApiResponse<ProductResponse> update(Integer id, ProductUpdateRequest req) throws IOException {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        if (req.getTitle() != null)       p.setTitle(req.getTitle());
        if (req.getDescription() != null) p.setDescription(req.getDescription());
        if (req.getPrice() != null)       p.setPrice(req.getPrice());

        if (req.getCategoryId() != null) {
            Category cat = categoryRepository.findById(req.getCategoryId())
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
            p.setCategory(cat);
        }

        if (req.getImage() != null && !req.getImage().isEmpty()) {
            String url = uploadImageFile.uploadImage(req.getImage());
            p.setImage(url);
        }


        if (req.getAddVariationIds() != null && !req.getAddVariationIds().isEmpty()) {
            var toAdd = productVariationRepository.findAllById(req.getAddVariationIds());
            var found = toAdd.stream().map(ProductVariation::getId).collect(Collectors.toSet());
            var missing = req.getAddVariationIds().stream().filter(i -> !found.contains(i)).toList();
            if (!missing.isEmpty()) {
                throw new AppException(ErrorCode.VARIATION_NOT_FOUND,"Variation không tồn tại: " + missing);
            }
            toAdd.forEach(v -> v.setProduct(p));
            productVariationRepository.saveAll(toAdd);
        }

        // BỚT variation (detach khỏi product này)
        if (req.getRemoveVariationIds() != null && !req.getRemoveVariationIds().isEmpty()) {
            var toRemove = productVariationRepository.findAllById(req.getRemoveVariationIds());
            toRemove.stream()
                    .filter(v -> v.getProduct() != null && v.getProduct().getProductId() == p.getProductId())
                    .forEach(v -> v.setProduct(null));
            productVariationRepository.saveAll(toRemove);
        }

        Product saved = productRepository.save(p);

        Set<Integer> currentVarIds = (saved.getProductVariations() == null) ? Set.of()
                : saved.getProductVariations().stream().map(ProductVariation::getId).collect(Collectors.toSet());

        return ApiResponse.<ProductResponse>builder()
                .code(1000).message("Success")
                .result(toResponse(saved))
                .build();
    }

    @Transactional
    public ApiResponse<Void> delete(Integer id) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new AppException((ErrorCode.PRODUCT_NOT_FOUND)));


        if (p.getProductVariations() != null && !p.getProductVariations().isEmpty()) {
            throw new AppException(ErrorCode.VARIATION_EXISTED);
        }

        productRepository.delete(p);
        return ApiResponse.<Void>builder()
                .code(1000).message("Deleted").result(null).build();
    }


    public ApiResponse<ProductResponse> get(Integer id) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new AppException((ErrorCode.PRODUCT_NOT_FOUND)));

        Set<Integer> varIds = (p.getProductVariations() == null) ? Set.of()
                : p.getProductVariations().stream().map(ProductVariation::getId).collect(Collectors.toSet());

        return ApiResponse.<ProductResponse>builder()
                .code(1000).message("Success")
                .result(toResponse(p))
                .build();
    }


    public ApiResponse<List<ProductResponse>> list() {
        var all = productRepository.findAll();

        var data = all.stream().map(p -> {
            Set<Integer> varIds = (p.getProductVariations() == null) ? Set.of()
                    : p.getProductVariations().stream().map(ProductVariation::getId).collect(Collectors.toSet());
            return toResponse(p);
        }).toList();

        return ApiResponse.<List<ProductResponse>>builder()
                .code(1000).message("Success")
                .result(data)
                .build();
    }

    private ProductResponse toResponse(Product p) {
        var variations = (p.getProductVariations() == null) ? List.<ProductVariationResponse>of()
                : p.getProductVariations().stream()
                .map(v -> ProductVariationResponse.builder()
                        .id(v.getId())
                        .productId(p.getProductId())
                        .image(v.getImage())
                        .size(v.getSize())
                        .color(v.getColor())
                        .stockQuantity(v.getStockQuantity())
                        .build())
                .toList();

        return ProductResponse.builder()
                .productId(p.getProductId())
                .title(p.getTitle())
                .description(p.getDescription())
                .price(p.getPrice())
                .image(p.getImage())
                .categoryId(p.getCategory() == null ? null : p.getCategory().getId())
                .variationCount(variations.size())
                .variations(variations)
                .build();
    }

    public ApiResponse<PageResult<ProductResponse>> search(
            String keyword,
            Double minPrice,
            Double maxPrice,
            List<String> sizes,
            List<String> colors,
            Pageable pageable) {

        // fallback size = 5 nếu không truyền hoặc <= 0
        if (pageable == null || pageable.getPageSize() <= 0) {
            int pageNum = (pageable == null) ? 0 : pageable.getPageNumber();
            var sort = (pageable == null) ? Sort.unsorted() : pageable.getSort();
            pageable = PageRequest.of(pageNum, 5, sort);
        }

        List<Specification<Product>> parts = Stream.<Specification<Product>>of(
                ProductSpecs.keyword(keyword),
                ProductSpecs.priceBetween(minPrice, maxPrice),
                ProductSpecs.variationSizeIn(sizes),
                ProductSpecs.variationColorContains(colors)
        ).filter(Objects::nonNull).toList();

        Specification<Product> spec = parts.isEmpty()
                ? null                                  // không filter
                : Specification.allOf(parts);

        Page<Product> page = productRepository.findAll(spec, pageable);

        List<ProductResponse> items = page.getContent().stream()
                .map(this::toResponse)
                .toList();


        var payload = PageResult.<ProductResponse>builder()
                .items(items)
                .page(page.getNumber())
                .size(page.getSize())
                .total(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();

        return ApiResponse.ok(payload);
    }

//    private Map<String, List<String>> encodeParams(String keyword, List<String> sizes, List<String> colors) {
//        var enc =Base64.getUrlEncoder(); // URL-safe
//        var map = new java.util.LinkedHashMap<String, List<String>>();
//        if (keyword != null && !keyword.isBlank()) {
//            map.put("keyword", List.of(enc.encodeToString(keyword.getBytes(java.nio.charset.StandardCharsets.UTF_8))));
//        }
//        if (sizes != null && !sizes.isEmpty()) {
//            map.put("sizes", sizes.stream()
//                    .map(s -> enc.encodeToString(s.getBytes(java.nio.charset.StandardCharsets.UTF_8)))
//                    .toList());
//        }
//        if (colors != null && !colors.isEmpty()) {
//            map.put("colors", colors.stream()
//                    .map(c -> enc.encodeToString(c.getBytes(java.nio.charset.StandardCharsets.UTF_8)))
//                    .toList());
//        }
//        return map;
//    }


}
