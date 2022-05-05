package ru.gx.core.jdbc.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.gx.core.jdbc.ActiveConnectionsContainer;
import ru.gx.core.jdbc.save.JdbcBinaryDbSavingOperator;
import ru.gx.core.jdbc.save.JdbcJsonDbSavingOperator;

@Configuration
@EnableConfigurationProperties(ConfigurationPropertiesService.class)
public class CommonAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public ActiveConnectionsContainer activeConnectionsContainer() {
        return new ActiveConnectionsContainer();
    }

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    @ConditionalOnMissingBean
    public JdbcJsonDbSavingOperator jdbcJsonDbSavingOperator(
            @NotNull final ObjectMapper objectMapper,
            @NotNull final ActiveConnectionsContainer activeConnectionsContainer
    ) {
        return new JdbcJsonDbSavingOperator(objectMapper, activeConnectionsContainer);
    }

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    @ConditionalOnMissingBean
    public JdbcBinaryDbSavingOperator jdbcBinaryDbSavingOperator(
            @NotNull final ObjectMapper objectMapper,
            @NotNull final ActiveConnectionsContainer activeConnectionsContainer
    ) {
        return new JdbcBinaryDbSavingOperator(objectMapper, activeConnectionsContainer);
    }
}
