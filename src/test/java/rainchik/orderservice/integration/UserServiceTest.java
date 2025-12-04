package rainchik.orderservice.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import rainchik.orderservice.config.TestcontainersConfiguration;
import rainchik.orderservice.dto.UserResponseDTO;
import rainchik.orderservice.service.UserService;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "user-service.base-url=http://localhost:8081/user"
})
public class UserServiceTest {

    private static WireMockServer wireMockServer;


    private UserService userService;

    @Autowired
    public UserServiceTest(UserService userService) {
        this.userService = userService;
    }

    @BeforeAll
    static void beforeAll() {
        wireMockServer = new WireMockServer(8081);
        wireMockServer.start();
        WireMock.configureFor("localhost", 8081);
    }

    @AfterAll
    static void afterAll() {
        wireMockServer.stop();
    }

    @Test
    void getUserByEmail_ShouldReturnUser_WhenUserServiceResponds() {


        // Arrange
        wireMockServer.stubFor(get(urlPathMatching("/user/email/test%40example.com"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": 123}")));

        // Act & Assert
        assertDoesNotThrow(() -> {
            UserResponseDTO result = userService.getUserByEmail("test@example.com");
            assertNotNull(result);
        });
    }

}
