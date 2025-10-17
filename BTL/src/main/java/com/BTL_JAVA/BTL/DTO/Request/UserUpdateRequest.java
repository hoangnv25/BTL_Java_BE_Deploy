package com.BTL_JAVA.BTL.DTO.Request;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Getter
@Setter
public class UserUpdateRequest {

    private String fullName;

    private String email;

    private String password;

    private String phoneNumber;

    private List<String> roles;

}
