package ru.gx.core.jdbc.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.gx.core.data.save.DbSavingOperator;
import ru.gx.core.jdbc.save.JdbcDbSaver;
import ru.gx.core.jdbc.sqlwrapping.JdbcThreadConnectionsWrapper;
import ru.gx.core.jdbc.save.JdbcBinaryDbSavingOperator;
import ru.gx.core.jdbc.save.JdbcJsonDbSavingOperator;

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
            value = "service.data-saving.operator.type",
            havingValue = "json"
    )
    public JdbcJsonDbSavingOperator jdbcJsonDbSavingOperator(
            @NotNull final ObjectMapper objectMapper,
            @NotNull final JdbcThreadConnectionsWrapper activeConnectionsContainer
    ) {
        return new JdbcJsonDbSavingOperator(objectMapper, activeConnectionsContainer);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(
            value = "service.db-saving.operator.type",
            havingValue = "binary"
    )
    public JdbcBinaryDbSavingOperator jdbcBinaryDbSavingOperator(
            @NotNull final ObjectMapper objectMapper,
            @NotNull final JdbcThreadConnectionsWrapper activeConnectionsContainer
    ) {
        return new JdbcBinaryDbSavingOperator(objectMapper, activeConnectionsContainer);
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
