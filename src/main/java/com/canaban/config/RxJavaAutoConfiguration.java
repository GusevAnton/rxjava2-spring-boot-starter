package com.canaban.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.method.support.AsyncHandlerMethodReturnValueHandler;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by antongusev on 15.03.17.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.canaban.handler", "com.canaban.exception", "com.canaban.subscriber"})
public class RxJavaAutoConfiguration {

    public static void main(String[] args) {
        SpringApplication.run(RxJavaAutoConfiguration.class, args);
    }

    @Configuration
    public static class RxJavaWebConfiguration {

        @Autowired
        private List<AsyncHandlerMethodReturnValueHandler> handlers = new ArrayList<AsyncHandlerMethodReturnValueHandler>();

        @Bean
        public WebMvcConfigurer rxJavaWebMvcConfiguration() {
            return new WebMvcConfigurerAdapter() {
                @Override
                public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
                    if (handlers != null) {
                        returnValueHandlers.addAll(handlers);
                    }
                }
            };
        }
    }

}
