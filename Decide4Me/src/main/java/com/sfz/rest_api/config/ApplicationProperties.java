package com.sfz.rest_api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "local")
public class ApplicationProperties {

    private String fileServer;
    private String serverAddress;
    private String staticURL;

}
