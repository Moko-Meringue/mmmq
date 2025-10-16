package org.mmmq;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestClient;

@Profile("!test")
@AutoConfiguration
@ComponentScan(basePackages = "org.mmmq")
public class MmmqAutoConfiguration {

    @Bean
    public RestClient restClient() {
        return RestClient.builder().build();
    }
}
