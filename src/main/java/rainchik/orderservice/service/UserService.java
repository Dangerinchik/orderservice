package rainchik.orderservice.service;

import rainchik.orderservice.dto.UserResponseDTO;

public interface UserService {
    public UserResponseDTO getUserByEmail(String email);
}
