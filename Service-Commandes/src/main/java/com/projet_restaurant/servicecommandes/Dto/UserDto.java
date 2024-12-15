package com.projet_restaurant.servicecommandes.Dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Data
public class UserDto {
    private Long id;
    private String username;
    //private String password;
    private String email;

}
