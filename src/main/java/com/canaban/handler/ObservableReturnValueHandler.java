package com.canaban.handler;

import com.canaban.config.Emmiter;
import io.reactivex.Observable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ObservableReturnValueHandler implements AsyncHandlerMethodReturnValueHandler {

    @Autowired
    private ExceptionHandler exceptionHandler;

    @Override
    public boolean isAsyncReturnValue(Object response, MethodParameter methodParameter) {
        return response != null && supportsReturnType(methodParameter);
    }

    @Override
    public boolean supportsReturnType(MethodParameter methodParameter) {
        return Observable.class.isAssignableFrom(methodParameter.getParameterType());
    }

    @Override
    public void handleReturnValue(Object response, MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest) throws Exception {
        final Observable observable = Observable.class.cast(response);
        if (methodParameter.getMethod().isAnnotationPresent(Emmiter.class)) {
            final ResponseBodyEmitter emitter = new ResponseBodyEmitter();
            final DeferredResult deferredResult = new DeferredResult();
            observable.subscribe(res -> emitter.send(res), error -> emitter.completeWithError((Throwable) error), emitter::complete);
            deferredResult.setResult(emitter);
            WebAsyncUtils.getAsyncManager(nativeWebRequest)
                    .startDeferredResultProcessing(deferredResult, modelAndViewContainer);
        } else {
            final DeferredResult deferredResult = new DeferredResult();
            observable.subscribe(deferredResult::setResult, error -> exceptionHandler.handleException(deferredResult, (Throwable) error));
            WebAsyncUtils.getAsyncManager(nativeWebRequest)
                    .startDeferredResultProcessing(deferredResult, modelAndViewContainer);
        }
    }
}
