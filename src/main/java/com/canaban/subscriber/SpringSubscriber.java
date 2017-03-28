package com.canaban.subscriber;

import io.reactivex.MaybeObserver;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Created by antongusev on 28.03.17.
 */
public interface SpringSubscriber extends Subscriber, MaybeObserver, SingleObserver, Observer {

    void onSubscribe(Subscription subscription);

    void onNext(Object o);

    void onSubscribe(Disposable d);

    void onSuccess(Object o);

    void onError(Throwable throwable);

    void onComplete();

    DeferredResult getDeferredResult();

}
