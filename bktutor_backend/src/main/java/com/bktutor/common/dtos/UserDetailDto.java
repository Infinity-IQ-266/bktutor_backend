package com.bktutor.common.dtos;

import com.bktutor.common.enums.Role;
import lombok.Data;

@Data
public class UserDetailDto {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String avatarUrl;
    private Role role;
}
