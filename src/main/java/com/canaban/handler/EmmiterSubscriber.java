package com.canaban.handler;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;

/**
 * Created by antongusev on 27.03.17.
 */
public class EmmiterSubscriber implements Subscriber {

    private ResponseBodyEmitter responseBodyEmitter;

    public ResponseBodyEmitter getResponseBodyEmmiter() {
        return responseBodyEmitter;
    }

    public EmmiterSubscriber(ResponseBodyEmitter responseBodyEmitter) {
        this.responseBodyEmitter = responseBodyEmitter;
    }

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
    public void onError(Throwable throwable) {
        responseBodyEmitter.completeWithError(throwable);
    }

    @Override
    public void onComplete() {
        responseBodyEmitter.complete();
    }
}
