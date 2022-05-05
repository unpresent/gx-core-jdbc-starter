package ru.gx.core.jdbc.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(prefix = "service")
@Getter
@Setter
public class ConfigurationPropertiesService {
    @NestedConfigurationProperty
    private ActiveConnectionsContainer activeConnectionsContainer;

    @Getter
    @Setter
    public static class ActiveConnectionsContainer {
        private boolean enabled;
    }
}
