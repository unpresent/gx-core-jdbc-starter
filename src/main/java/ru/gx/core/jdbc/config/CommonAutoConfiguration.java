package ru.gx.core.jdbc.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.gx.core.jdbc.save.JdbcBinaryDbSavingOperator;
import ru.gx.core.jdbc.save.JdbcDbSaver;
import ru.gx.core.jdbc.save.JdbcJsonDbSavingOperator;
import ru.gx.core.jdbc.sqlwrapping.JdbcThreadConnectionsWrapper;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(ConfigurationPropertiesService.class)
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class CommonAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public JdbcThreadConnectionsWrapper jdbcThreadConnectionsWrapper(
            @NotNull final DataSource dataSource
    ) {
        return new JdbcThreadConnectionsWrapper(dataSource);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(
            value = "service.db-saving.jdbc-json-operator.enabled",
            havingValue = "true"
    )
    public JdbcJsonDbSavingOperator jdbcJsonDbSavingOperator(
            @NotNull final ObjectMapper objectMapper,
            @NotNull final JdbcThreadConnectionsWrapper threadConnectionsWrapper
    ) {
        return new JdbcJsonDbSavingOperator(objectMapper, threadConnectionsWrapper);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(
            value = "service.db-saving.jdbc-binary-operator.enabled",
            havingValue = "true"
    )
    public JdbcBinaryDbSavingOperator jdbcBinaryDbSavingOperator(
            @NotNull final ObjectMapper objectMapper,
            @NotNull final JdbcThreadConnectionsWrapper threadConnectionsWrapper
    ) {
        return new JdbcBinaryDbSavingOperator(objectMapper, threadConnectionsWrapper);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(
            value = "service.db-saving.jdbc-saver.enabled",
            havingValue = "true"
    )
    public JdbcDbSaver jdbcDbSaver() {
        return new JdbcDbSaver();
    }
}
