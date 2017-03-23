package com.canaban.handler;

import com.canaban.config.Emmiter;
import com.canaban.dto.Subscriber;
import com.canaban.exception.ExceptionHandler;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncUtils;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

/**
 * Created by antongusev on 22.03.17.
 */
public interface DefaultHandler {

    default void handle(Object response, MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest) throws Exception{
        Subscriber subscriber = new Subscriber(response);
        if (methodParameter.getMethod().isAnnotationPresent(Emmiter.class)) {
            final ResponseBodyEmitter emitter = new ResponseBodyEmitter();
            final DeferredResult deferredResult = new DeferredResult();
            subscriber.subscribe(res -> emitter.send(res), error -> emitter.completeWithError((Throwable) error), emitter::complete);
            deferredResult.setResult(emitter);
            WebAsyncUtils.getAsyncManager(nativeWebRequest)
                    .startDeferredResultProcessing(deferredResult, modelAndViewContainer);
        } else {
            final DeferredResult deferredResult = new DeferredResult();
            subscriber.subscribe(deferredResult::setResult, error -> getExceptionHandler().handleException(deferredResult, (Throwable) error));
            WebAsyncUtils.getAsyncManager(nativeWebRequest)
                    .startDeferredResultProcessing(deferredResult, modelAndViewContainer);
        }
    }

    ExceptionHandler getExceptionHandler();

}
