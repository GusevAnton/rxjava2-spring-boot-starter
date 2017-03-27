package com.canaban.handler;

import com.canaban.config.Emmiter;
import io.reactivex.parallel.ParallelFlowable;
import org.reactivestreams.Subscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncUtils;
import org.springframework.web.method.support.AsyncHandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by antongusev on 28.03.17.
 */
@Component
public class ParallelFlowableValuehandler implements AsyncHandlerMethodReturnValueHandler {

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
        return ParallelFlowable.class.isAssignableFrom(methodParameter.getParameterType());
    }

    @Override
    public void handleReturnValue(Object response, MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest) throws Exception {
        ParallelFlowable parallelFlowable = (ParallelFlowable) response;
        DeferredResult deferredResult;
        Subscriber[] subscribers = new Subscriber[parallelFlowable.parallelism()];
        if (methodParameter.getMethod().isAnnotationPresent(Emmiter.class)) {
            deferredResult = emmiterSubscriber.getDeferredResult();
            Arrays.fill(subscribers, emmiterSubscriber);
        } else {
            deferredResult = deferredSubscriber.getDeferredResult();
            parallelFlowable.sequential().reduce(new ArrayList(), (list, value) -> {
                ((ArrayList) list).add(value);
                return list;
            }).subscribe(deferredResult::setResult);
        }
        WebAsyncUtils.getAsyncManager(nativeWebRequest)
                .startDeferredResultProcessing(deferredResult, modelAndViewContainer);
    }
}

