package com.canaban.handler;

import com.canaban.config.Emmiter;
import com.canaban.subscriber.DeferredSubscriber;
import com.canaban.subscriber.EmmiterSubscriber;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.method.support.AsyncHandlerMethodReturnValueHandler;

/**
 * Created by antongusev on 28.03.17.
 */
public interface DefaultHandler extends AsyncHandlerMethodReturnValueHandler {

    default DeferredResult handle(Flowable flowable, MethodParameter methodParameter) {
        DeferredResult deferredResult;
        if (methodParameter.getMethod().isAnnotationPresent(Emmiter.class)) {
            deferredResult = getEmmiterSubscriber().getDeferredResult();
            flowable.subscribe(getEmmiterSubscriber());
        } else {
            deferredResult = getDeferredSubscriber().getDeferredResult();
            flowable.subscribe(getDeferredSubscriber());
        }
        return deferredResult;
    }

    default DeferredResult handle(Single single, MethodParameter methodParameter) {
        DeferredResult deferredResult;
        if (methodParameter.getMethod().isAnnotationPresent(Emmiter.class)) {
            deferredResult = getEmmiterSubscriber().getDeferredResult();
            single.subscribe(getEmmiterSubscriber());
        } else {
            deferredResult = getDeferredSubscriber().getDeferredResult();
            single.subscribe(getDeferredSubscriber());
        }
        return deferredResult;
    }

    default DeferredResult handle(Observable observable, MethodParameter methodParameter) {
        DeferredResult deferredResult;
        if (methodParameter.getMethod().isAnnotationPresent(Emmiter.class)) {
            deferredResult = getEmmiterSubscriber().getDeferredResult();
            observable.subscribe(getEmmiterSubscriber());
        } else {
            deferredResult = getDeferredSubscriber().getDeferredResult();
            observable.subscribe(getDeferredSubscriber());
        }
        return deferredResult;
    }

    default DeferredResult handle(Maybe maybe, MethodParameter methodParameter) {
        DeferredResult deferredResult;
        if (methodParameter.getMethod().isAnnotationPresent(Emmiter.class)) {
            deferredResult = getEmmiterSubscriber().getDeferredResult();
            maybe.subscribe(getEmmiterSubscriber());
        } else {
            deferredResult = getDeferredSubscriber().getDeferredResult();
            maybe.subscribe(getDeferredSubscriber());
        }
        return deferredResult;
    }

    DeferredSubscriber getDeferredSubscriber();

    EmmiterSubscriber getEmmiterSubscriber();

}
