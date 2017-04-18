package com.canaban.handler;

import com.canaban.subscriber.DeferredSubscriber;
import io.reactivex.Observable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.WebAsyncUtils;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Created by antongusev on 15.03.17.
 */
@Slf4j
@Component
public class ObservableReturnValueHandler implements DefaultHandler {

    @Autowired
    private DeferredSubscriber deferredSubscriber;

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
        Observable observable = (Observable) response;
        observable.subscribe(getDeferredSubscriber());
        WebAsyncUtils.getAsyncManager(nativeWebRequest)
                .startDeferredResultProcessing(getDeferredSubscriber().getDeferredResult(), modelAndViewContainer);
    }

    @Override
    public DeferredSubscriber getDeferredSubscriber() {
        return deferredSubscriber;
    }

}
