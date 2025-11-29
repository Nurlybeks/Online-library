package org.example.market.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordDto {

    private String username;
    private String oldPassword;
    private String newPassword;
}
