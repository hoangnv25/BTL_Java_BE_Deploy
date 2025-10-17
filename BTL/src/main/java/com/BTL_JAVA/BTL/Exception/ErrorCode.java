package com.BTL_JAVA.BTL.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    USER_EXISTED(1002, "User exists", HttpStatus.BAD_REQUEST),
    UNCATEGORIED_EXCEPTION(9999, "Khong xac dinh", HttpStatus.INTERNAL_SERVER_ERROR),
    USERNAME_INVALID(1003, "Username must be at least 8 characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_KEY(1001, "invalid key", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not exists", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "Khong co quyen truy cap", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "You age must be at leats {min}", HttpStatus.BAD_REQUEST),
    REVIEW_NOT_FOUND(1009, "Review is not exists", HttpStatus.NOT_FOUND),
    INVALID_VARIATION(1010,"Variation is invalid",HttpStatus.BAD_REQUEST),
    DUPLICATE_VARIATION(1011,"Duplicate variation",HttpStatus.BAD_REQUEST),
    VARIATION_NOT_FOUND(1012,"Variation is not exists",HttpStatus.NOT_FOUND),
    CATEGORY_NOT_FOUND  (1013,"Category is not exists",HttpStatus.NOT_FOUND),
    PRODUCT_NOT_FOUND(1014,"Product is not exists",HttpStatus.NOT_FOUND),
    PRODUCT_EXISTED(1013,"Product already exists",HttpStatus.BAD_REQUEST),
    VARIATION_EXISTED(1014,"Variation already exists",HttpStatus.BAD_REQUEST),

    //Sales
    SALE_NOT_EXISTED(2001, "Sale khong ton tai", HttpStatus.NOT_FOUND),
    SALES_NOT_FOUND(2002, "khong tim thay Sale", HttpStatus.NOT_FOUND),
    INVALID_SALE_DATE(2003, "End date khong the truoc St date", HttpStatus.BAD_REQUEST),
    INVALID_SALE_VALUE(2004, "value khong duoc am", HttpStatus.BAD_REQUEST),
    INVALID_SALE_NAME(2005, "Bat buoc phai co ten", HttpStatus.BAD_REQUEST),
    PRODUCT_ALREADY_IN_SALE(2006, "San pham da ton tai trong Sale", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_IN_SALE(2007, "Khong tim thay san pham trong Sale", HttpStatus.BAD_REQUEST),
    ADD_TO_SALE_FAILED(2008, "Them san pham that bai", HttpStatus.INTERNAL_SERVER_ERROR),
    REMOVE_FROM_SALE_FAILED(2009, "Xoa san pham that bai", HttpStatus.INTERNAL_SERVER_ERROR),
    PRODUCT_NOT_EXISTED(3001, "San pham khong ton tai", HttpStatus.NOT_FOUND),
    SALE_OVERLAPPING(2010, "Đa co sale hoat đong trong thoi gian nay", HttpStatus.BAD_REQUEST),
    CREATE_FAILED(4001, "Tao that bai", HttpStatus.INTERNAL_SERVER_ERROR),
    UPDATE_FAILED(4002, "Cap nhat that bai", HttpStatus.INTERNAL_SERVER_ERROR),
    DELETE_FAILED(4003, "Xoa that bai", HttpStatus.INTERNAL_SERVER_ERROR),

    // Cart
    CART_NOT_FOUND(5001, "Khong thay cart", HttpStatus.NOT_FOUND),
    INVALID_QUANTITY(5002, "So luong phai lon hon 0", HttpStatus.BAD_REQUEST),
    PRODUCT_VARIATION_NOT_FOUND(5003, "Khong tim thay variation", HttpStatus.NOT_FOUND),
    NOT_ENOUGH_STOCK(5004, "Trong kho khong du", HttpStatus.BAD_REQUEST),
    CART_UPDATE_FAILED(5005, "Update that bai", HttpStatus.INTERNAL_SERVER_ERROR),

    //Address
    ADDRESS_NOT_FOUND(6001, "Address not found", HttpStatus.NOT_FOUND),
    CANNOT_REMOVE_DEFAULT_ADDRESS(6007, "Khong cap nhat duoc do khong co DefaultAddress.", HttpStatus.BAD_REQUEST);
    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private int code;
    private String message;
    private HttpStatusCode statusCode;
}