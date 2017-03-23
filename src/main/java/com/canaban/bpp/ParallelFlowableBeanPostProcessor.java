package com.canaban.bpp;

import io.reactivex.Scheduler;
import io.reactivex.parallel.ParallelFlowable;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.Controller;

/**
 * Created by antongusev on 24.03.17.
 */
public class ParallelFlowableBeanPostProcessor implements BeanPostProcessor {

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
                if (method.getReturnType().equals(ParallelFlowable.class)) {
                    ParallelFlowable obervable = (ParallelFlowable) method.invoke(bean, objects);
                    return obervable.runOn(scheduler);
                } else
                    return method.invoke(bean, objects);
            });
        } else {
            return bean;
        }
    }
}
