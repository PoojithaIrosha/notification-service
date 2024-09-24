package com.poojithairosha.notification.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic emailNotificationTopic() {
        return TopicBuilder.name("email-notification").build();
    }

    @Bean
    public NewTopic smsNotificationTopic() {
        return TopicBuilder.name("sms-notification").build();
    }

    @Bean
    public NewTopic whatsappNotificationTopic() {
        return TopicBuilder.name("whatsapp-notification").build();
    }
}
