package rainchik.orderservice.service.impl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;
import rainchik.orderservice.dto.UserResponseDTO;
import rainchik.orderservice.exception.UserServiceNotAvailableException;
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
    @CircuitBreaker(name = "getUserByEmail", fallbackMethod = "fallbackGetUserByEmail")
    public UserResponseDTO getUserByEmail(String email) {
        try {
            System.out.println("=== НАЧАЛО getUserByEmail ===");
            System.out.println("Base URL: " + restClient);

            Map<String, Object> map = restClient.get()
                    .uri("/user/email/{email}", email)
                    .retrieve()
                    .onStatus(HttpStatus.NOT_FOUND::equals, ((request, response) ->{
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found");
                    }))
                    .onStatus(HttpStatus.SERVICE_UNAVAILABLE::equals, ((request, response) ->{
                        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Service unavailable");
                    }))
                    .body(Map.class);

            System.out.println("Получен ответ: " + map);

            UserResponseDTO dto = new UserResponseDTO();

            Object id = map.get("id");
            if (id instanceof Integer) {
                dto.setId(((Integer) id).longValue());
            } else if (id instanceof Long) {
                dto.setId((Long) id);
            } else {
                throw new RuntimeException("Unexpected ID type: " + (id != null ? id.getClass().getName() : "null"));
            }

            System.out.println("=== УСПЕШНОЕ ЗАВЕРШЕНИЕ getUserByEmail ===");
            return dto;

        } catch (Exception e) {
            System.out.println("=== ОШИБКА В getUserByEmail ===");
            System.out.println("Тип ошибки: " + e.getClass().getName());
            System.out.println("Сообщение: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public UserResponseDTO fallbackGetUserByEmail(String email, Throwable throwable) throws UserServiceNotAvailableException {
        System.out.println("=== FALLBACK ВЫЗВАН ===");
        System.out.println("Email: " + email);
        if (throwable != null) {
            System.out.println("Причина fallback: " + throwable.getClass().getName());
            System.out.println("Сообщение: " + throwable.getMessage());
            throwable.printStackTrace();
        }
        throw new UserServiceNotAvailableException();
    }
}
