package com.example.pancharm.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sendgrid.SendGrid;

@Configuration
public class SendgridConfig {

    @Value("${sendgrid.api-key}")
    private String sendgridApiKey;

    @Bean
    public SendGrid sendGrid() {
        return new SendGrid(sendgridApiKey);
    }
}
