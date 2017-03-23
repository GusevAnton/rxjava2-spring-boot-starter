package com.canaban.handler;

import com.canaban.exception.ExceptionHandler;
import io.reactivex.parallel.ParallelFlowable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.AsyncHandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Created by antongusev on 24.03.17.
 */
@Component
public class ParallelFlowableReturnHandler implements AsyncHandlerMethodReturnValueHandler, DefaultHandler{

    @Autowired
    private ExceptionHandler exceptionHandler;

    @Override
    public ExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    @Override
    public boolean isAsyncReturnValue(Object returnValue, MethodParameter returnType) {
        return returnValue != null && supportsReturnType(returnType);
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return ParallelFlowable.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        handle(returnValue, returnType, mavContainer, webRequest);
    }
}
