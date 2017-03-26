package com.canaban.handler;

import com.canaban.config.Emmiter;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncUtils;
import org.springframework.web.method.support.AsyncHandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

/**
 * Created by antongusev on 15.03.17.
 */
@Slf4j
@Component
public class RxReturnValueHandler implements AsyncHandlerMethodReturnValueHandler {

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
        DeferredResult deferredResult = new DeferredResult();
        if (methodParameter.getMethod().isAnnotationPresent(Emmiter.class)) {
            ResponseBodyEmitter responseBodyEmitter = new ResponseBodyEmitter();
            EmmiterSubscriber emmiterSubscriber = new EmmiterSubscriber(responseBodyEmitter);
            publisher.subscribe(emmiterSubscriber);
            deferredResult.setResult(responseBodyEmitter);
        } else
            publisher.subscribe(new DeferredSubscriber(deferredResult));
        WebAsyncUtils.getAsyncManager(nativeWebRequest)
                .startDeferredResultProcessing(deferredResult, modelAndViewContainer);
    }

}
