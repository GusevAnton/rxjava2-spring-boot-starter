package com.canaban.handler;

import com.canaban.exception.ExceptionHandler;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Created by antongusev on 27.03.17.
 */
@Component
@Scope(scopeName = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DeferredSubscriber implements Subscriber {

    private DeferredResult deferredResult = new DeferredResult();

    @Autowired
    private ExceptionHandler exceptionHandler;

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
        System.out.println("COMPLETE");
    }

    public DeferredResult getDeferredResult() {
        return deferredResult;
    }
}

