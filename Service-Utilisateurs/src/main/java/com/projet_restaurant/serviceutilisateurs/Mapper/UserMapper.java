package com.projet_restaurant.serviceutilisateurs.Mapper;

import com.projet_restaurant.serviceutilisateurs.Dto.UserDTO;
import com.projet_restaurant.serviceutilisateurs.Entity.User;
import com.projet_restaurant.serviceutilisateurs.Entity.Role;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    public UserDTO toDto(User user) {
        if (user == null) {
            return null;
        }
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }

    public User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        return new User(dto.getId(), dto.getUsername(), dto.getEmail(), dto.getRole());

    }
}

