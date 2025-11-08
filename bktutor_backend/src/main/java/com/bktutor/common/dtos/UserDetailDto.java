package com.bktutor.common.dtos;

import com.bktutor.common.enums.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDetailDto {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String avatarUrl;
    private Role role;
}
