package com.canaban.subscriber;

import io.reactivex.disposables.Disposable;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;

/**
 * Created by antongusev on 27.03.17.
 */
@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class EmmiterSubscriber implements SpringSubscriber {

    private DeferredResult deferredResult = new DeferredResult();

    private ResponseBodyEmitter responseBodyEmitter = new ResponseBodyEmitter();

    @Override
    public void onSubscribe(Subscription subscription) {
        subscription.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(Object o) {
        try {
            responseBodyEmitter.send(o);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onSuccess(Object o) {

    }

    @Override
    public void onError(Throwable throwable) {
        responseBodyEmitter.completeWithError(throwable);
    }

    @Override
    public void onComplete() {
        responseBodyEmitter.complete();
    }

    public DeferredResult getDeferredResult() {
        deferredResult.setResult(responseBodyEmitter);
        return deferredResult;
    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean isDisposed() {
        return true;
    }
}
