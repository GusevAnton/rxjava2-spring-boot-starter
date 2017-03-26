package com.canaban.handler;

import com.canaban.exception.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Created by antongusev on 27.03.17.
 */
//@Component
//@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class DeferredSubscriber implements Subscriber {

    private DeferredResult deferredResult;

    @Autowired
    private ExceptionHandler exceptionHandler;

    public DeferredSubscriber(DeferredResult deferredResult) {
        this.deferredResult = deferredResult;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        subscription.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(Object o) {
        deferredResult.setResult(o);
    }

    @Override
    public void onError(Throwable throwable) {
        exceptionHandler.handleException(deferredResult, throwable);
    }

    @Override
    public void onComplete() {
    }
}
