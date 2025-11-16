package rainchik.orderservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;
import rainchik.orderservice.dto.UserResponseDTO;
import rainchik.orderservice.service.UserService;

import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private final RestClient restClient;

    @Autowired
    public UserServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public UserResponseDTO getUserByEmail(String email) {
        Map<String, Object> map = restClient.get()
                .uri("/email/{email}", email)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, ((request, response) ->{
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found");
                }))
                .onStatus(HttpStatus.SERVICE_UNAVAILABLE::equals, ((request, response) ->{
                    throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Service unavailable");
                }))
                .body(Map.class);
        UserResponseDTO dto = new UserResponseDTO();

        dto.setId((Long) map.get("id"));

        return dto;
    }
}
