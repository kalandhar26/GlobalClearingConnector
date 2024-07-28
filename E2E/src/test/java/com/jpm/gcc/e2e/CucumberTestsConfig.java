package com.jpm.gcc.e2e;

import com.jpm.gcc.e2e.embedded.kafka.KafkaServer;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;

import javax.sql.DataSource;

@Configuration
@ComponentScan({
})
@EnableAutoConfiguration
public class CucumberTestsConfig {


    @Bean
    public DataSource dataSource(){
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(System.getProperty("local.db.jdbc.url"));
        dataSource.setDriverClassName(System.getProperty("local.db.jdbc.drv"));
        dataSource.setUsername(System.getProperty("local.db.jdbc.usr"));
        dataSource.setPassword(System.getProperty("local.db.jdbc.pwd"));
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource){
    return new JdbcTemplate(dataSource);
    }

    @Bean
    @Primary
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(JdbcTemplate jdbcTemplate){
        return new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(){
        return KafkaServer.createProducer();
    }
}
