package com.volunteerplatform.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginDTO {

    private String username;
    private String password;
    private String email;
}
