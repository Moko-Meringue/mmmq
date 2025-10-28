package org.mmmq.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@AutoConfiguration
@ComponentScan(basePackages = "org.mmmq")
public class MMMQAutoConfiguration {

/*
    @Bean
    public RestClient restClient() {
        return RestClient.builder().build();
    }
*/
}
