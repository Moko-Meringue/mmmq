package org.mmmq.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.web.client.MockServerRestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

@Configuration
@Profile("test")
@EnableAutoConfiguration
public class TestConfiguration {

    private MockRestServiceServer mockRestServiceServer;

    @Bean
    public RestClient mockServerClient() {
        MockServerRestClientCustomizer customizer = new MockServerRestClientCustomizer();
        RestClient.Builder builder = RestClient.builder();
        customizer.customize(builder);
        RestClient restClient = builder.build();
        mockRestServiceServer = customizer.getServer();
        return restClient;
    }

    @Bean
    public MockRestServiceServer server() {
        return mockRestServiceServer;
    }
}
