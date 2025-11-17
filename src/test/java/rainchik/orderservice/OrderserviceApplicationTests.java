package rainchik.orderservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import rainchik.orderservice.config.TestcontainersConfiguration;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@ActiveProfiles("test")
class OrderserviceApplicationTests {

    @Test
    void contextLoads() {

    }

}
