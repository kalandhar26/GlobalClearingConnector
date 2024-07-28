package com.jpm.gcc.e2e.embedded.orches;

import com.jpm.gcc.e2e.embedded.kafka.MockCloudKafkaManagerConfiguration;
import org.mockito.Mockito;
import org.springframework.cloud.netflix.eureka.CloudEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class OrchestrationConfig {

    @Bean
    public CloudEurekaClient eurekaClient(){
        return Mockito.mock(CloudEurekaClient.class);
    }

    @Bean
    @Primary
    public MockCloudKafkaManagerConfiguration cloudKafkaManagerConfiguration(){
        return new MockCloudKafkaManagerConfiguration();
    }

}
