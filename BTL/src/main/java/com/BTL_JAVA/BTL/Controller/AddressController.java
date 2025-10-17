package com.BTL_JAVA.BTL.Controller;

import com.BTL_JAVA.BTL.DTO.Request.AddressRequest;
import com.BTL_JAVA.BTL.DTO.Response.AddressResponse;
import com.BTL_JAVA.BTL.Service.AddressService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressController {

    AddressService addressService;

    @GetMapping("/default")
    @PreAuthorize("isAuthenticated()")
    public AddressResponse getDefaultAddress() {
        return addressService.getDefaultAddress();
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<AddressResponse> getAllAddresses() {
        return addressService.getAllAddresses();
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public AddressResponse createAddress(@RequestBody AddressRequest request) {
        return addressService.createAddress(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public AddressResponse updateAddress(
            @PathVariable Integer id,
            @RequestBody AddressRequest request) {
        return addressService.updateAddress(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public void deleteAddress(@PathVariable Integer id) {
        addressService.deleteAddress(id);
    }
}