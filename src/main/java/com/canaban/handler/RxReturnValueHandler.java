package com.canaban.handler;

import com.canaban.config.Emmiter;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncUtils;
import org.springframework.web.method.support.AsyncHandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Created by antongusev on 15.03.17.
 */
@Slf4j
@Component
public class RxReturnValueHandler implements AsyncHandlerMethodReturnValueHandler {

    @Autowired
    private DeferredSubscriber deferredSubscriber;

    @Autowired
    private EmmiterSubscriber emmiterSubscriber;

    @Override
    public boolean isAsyncReturnValue(Object response, MethodParameter methodParameter) {
        return response != null && supportsReturnType(methodParameter);
    }

    @Override
    public boolean supportsReturnType(MethodParameter methodParameter) {
        return Publisher.class.isAssignableFrom(methodParameter.getParameterType());
    }

    @Override
    public void handleReturnValue(Object response, MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest) throws Exception {
        Publisher publisher = (Publisher) response;
        DeferredResult deferredResult;
        if (methodParameter.getMethod().isAnnotationPresent(Emmiter.class)) {
            deferredResult = emmiterSubscriber.getDeferredResult();
            publisher.subscribe(emmiterSubscriber);
        } else {
            deferredResult = deferredSubscriber.getDeferredResult();
            publisher.subscribe(deferredSubscriber);
        }
        WebAsyncUtils.getAsyncManager(nativeWebRequest)
                .startDeferredResultProcessing(deferredResult, modelAndViewContainer);
    }

}
