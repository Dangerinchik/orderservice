package rainchik.orderservice;

import org.springframework.boot.SpringApplication;
import rainchik.orderservice.config.TestcontainersConfiguration;

public class TestOrderserviceApplication {

    public static void main(String[] args) {
        SpringApplication.from(OrderserviceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
