package com.canaban.handler;

import com.canaban.exception.ExceptionHandler;
import io.reactivex.Maybe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.AsyncHandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Created by antongusev on 17.03.17.
 */
@Component
public class MaybeReturnValueHandler implements AsyncHandlerMethodReturnValueHandler, DefaultHandler {

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
        handle(response, methodParameter, modelAndViewContainer, nativeWebRequest);
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }
}
