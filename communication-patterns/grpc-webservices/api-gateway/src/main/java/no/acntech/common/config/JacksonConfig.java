package no.acntech.common.config;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.hubspot.jackson.datatype.protobuf.ProtobufModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer configureProtobufSupport() {
        return builder -> {
            builder.modulesToInstall(new ProtobufModule());
            builder.propertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE); // Protobuf module default uses lowercase underscore format, so need to change that
        };
    }
}
