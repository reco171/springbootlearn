package com.reco.web.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by root on 9/6/18.
 */
@SpringBootApplication
public class SampleWebUiApplication {

    @Bean
    public MessageRepository messageRepository() {
        return new InMemoryMessageRepository();
    }

    @Bean
    public Converter<String, Message> messageConverter() {
        return new Converter<String, Message>() {
            @Override
            public Message convert(String id) {
                return messageRepository().findMessage(Long.valueOf(id));
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(SampleWebUiApplication.class, args);
    }

}
