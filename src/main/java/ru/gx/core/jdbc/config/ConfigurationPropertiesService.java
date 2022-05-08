package ru.gx.core.jdbc.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(prefix = "service.db-saving")
@Getter
@Setter
public class ConfigurationPropertiesService {
    @NestedConfigurationProperty
    private JdbcSaver jdbcSaver;

    @NestedConfigurationProperty
    private Operator operator;

    @Getter
    @Setter
    public static class JdbcSaver {
        private boolean enabled = true;
    }

    @Getter
    @Setter
    public static class Operator {
        public OperatorType operatorType = OperatorType.Json;
    }

    @SuppressWarnings("unused")
    public enum OperatorType {
        Json,
        Binary
    }
}
