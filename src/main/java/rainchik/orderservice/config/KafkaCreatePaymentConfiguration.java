package rainchik.orderservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.mapping.Jackson2JavaTypeMapper;

@Configuration
public class KafkaCreatePaymentConfiguration {

    @Bean
    JsonMessageConverter jsonMessageConverter(ObjectMapper objectMapper) {

        JsonMessageConverter converter = new StringJsonMessageConverter(objectMapper);

        Jackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);
        typeMapper.addTrustedPackages("rainchik.orderservice.dto");

        converter.setTypeMapper(typeMapper);

        return converter;

    }

}
