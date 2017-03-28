package com.canaban.handler;

import com.canaban.subscriber.DeferredSubscriber;
import com.canaban.subscriber.EmmiterSubscriber;
import io.reactivex.Single;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncUtils;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Created by antongusev on 15.03.17.
 */
@Slf4j
@Component
public class SingleReturnValueHandler implements DefaultHandler {

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
        return Single.class.isAssignableFrom(methodParameter.getParameterType());
    }

    @Override
    public void handleReturnValue(Object response, MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest) throws Exception {
        Single single = (Single) response;
        DeferredResult deferredResult = handle(single, methodParameter);
        WebAsyncUtils.getAsyncManager(nativeWebRequest)
                .startDeferredResultProcessing(deferredResult, modelAndViewContainer);
    }

    @Override
    public DeferredSubscriber getDeferredSubscriber() {
        return deferredSubscriber;
    }

    @Override
    public EmmiterSubscriber getEmmiterSubscriber() {
        return emmiterSubscriber;
    }
}
