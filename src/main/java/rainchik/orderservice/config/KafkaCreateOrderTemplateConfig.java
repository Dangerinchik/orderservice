package rainchik.orderservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

@Configuration
public class KafkaCreateOrderTemplateConfig {

    @Bean
    JsonMessageConverter jsonMessageConverter(ObjectMapper objectMapper) {

        return new StringJsonMessageConverter(objectMapper);

    }

}
