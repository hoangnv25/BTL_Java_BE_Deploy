package com.BTL_JAVA.BTL.DTO.Request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {

    @Size(min = 2,message = "USERNAME_INVALID")
    private String fullName;

    private String email;

    @Size(min = 3,message = "INVALID_PASSWORD")
    private String password;

    private String phoneNumber;

}
