package com.canaban.handler;

import io.reactivex.Maybe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncUtils;
import org.springframework.web.method.support.AsyncHandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Created by antongusev on 17.03.17.
 */
@Component
public class MaybeReturnValueHandler implements AsyncHandlerMethodReturnValueHandler {

    @Autowired
    private ExceptionHandler exceptionHandler;

    @Override
    public boolean isAsyncReturnValue(Object response, MethodParameter methodParameter) {
        return response != null && supportsReturnType(methodParameter);
    }

    @Override
    public boolean supportsReturnType(MethodParameter methodParameter) {
        return Maybe.class.isAssignableFrom(methodParameter.getParameterType());
    }

    @Override
    public void handleReturnValue(Object response, MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest) throws Exception {
        final Maybe single = (Maybe) response;
        final DeferredResult deferredResult = new DeferredResult();
        single.subscribe(deferredResult::setResult, error -> exceptionHandler.handleException(deferredResult, (Throwable) error));
        WebAsyncUtils.getAsyncManager(nativeWebRequest)
                .startDeferredResultProcessing(deferredResult, modelAndViewContainer);
    }
}
