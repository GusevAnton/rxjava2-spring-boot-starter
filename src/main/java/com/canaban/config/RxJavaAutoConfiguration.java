package com.canaban.config;

import com.canaban.handler.FlowableReturnValueHandler;
import com.canaban.handler.MaybeReturnValueHandler;
import com.canaban.handler.ObservableReturnValueHandler;
import com.canaban.handler.SingleReturnValueHandler;
import io.reactivex.*;
import io.reactivex.schedulers.Schedulers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.AsyncHandlerMethodReturnValueHandler;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by antongusev on 15.03.17.
 */
@ComponentScan(basePackages = {"com.canaban.bpp", "com.canaban.handler", "com.canaban.exception"})
public class RxJavaAutoConfiguration {

    public static void main(String[] args) {
        SpringApplication.run(RxJavaAutoConfiguration.class, args);
    }

    @Bean
    @ConditionalOnClass(Flowable.class)
    public FlowableReturnValueHandler flowableReturnValueHandler() {
        return new FlowableReturnValueHandler();
    }

    @Bean
    @ConditionalOnClass(Single.class)
    public SingleReturnValueHandler singleReturnValueHandler() {
        return new SingleReturnValueHandler();
    }

    @Bean
    @ConditionalOnClass(Maybe.class)
    public MaybeReturnValueHandler maybeReturnValueHandler() {
        return new MaybeReturnValueHandler();
    }

    @Bean
    @ConditionalOnClass(Observable.class)
    public ObservableReturnValueHandler observableReturnValueHandler() {
        return new ObservableReturnValueHandler();
    }

    @Bean
    public Scheduler schedulers() {
        return Schedulers.computation();
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
