package com.canaban.bpp;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.Controller;

/**
 * Created by antongusev on 15.03.17.
 */
@Component
public class FlowableBeanPostProcessor implements BeanPostProcessor {

    @Autowired
    private Scheduler scheduler;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String s) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String s) throws BeansException {
        Class clazz = bean.getClass();
        if (clazz.isAnnotationPresent(RestController.class) || clazz.isAnnotationPresent(Controller.class)) {
            return Enhancer.create(clazz, (InvocationHandler) (o, method, objects) -> {
                if (method.getReturnType().equals(Flowable.class)) {
                    Flowable flowable = (Flowable) method.invoke(bean, objects);
                    return flowable.subscribeOn(scheduler);
                } else
                    return method.invoke(bean, objects);
            });
        } else {
            return bean;
        }
    }
}
