package com.canaban.dto;

import com.canaban.exception.SingleNotPermitedException;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.operators.flowable.FlowableSubscribeOn;
import io.reactivex.internal.operators.maybe.MaybeSubscribeOn;
import io.reactivex.internal.operators.observable.ObservableSubscribeOn;
import io.reactivex.internal.operators.parallel.ParallelMap;
import io.reactivex.internal.operators.single.SingleSubscribeOn;
import io.reactivex.parallel.ParallelFlowable;

/**
 * Created by antongusev on 22.03.17.
 */
public class Subscriber {

    private Flowable flowable;

    private Observable observable;

    private Maybe maybe;

    private Single single;

    private Class currentClass;

    private ParallelFlowable parallelFlowable;

    public Subscriber(Object response) {
        currentClass = response.getClass();
        if (response instanceof Flowable)
            flowable = (Flowable) response;
        else if (response instanceof Single)
            single = (Single) response;
        else if (response instanceof Maybe)
            maybe = (Maybe) response;
        else if (response instanceof Observable)
            observable = (Observable) response;
        else if (response instanceof ParallelFlowable)
            parallelFlowable = (ParallelFlowable) response;
    }

    public void subscribe(Consumer onNext, Consumer onError, Action onComplete) {
        if (currentClass.equals(FlowableSubscribeOn.class))
            flowable.subscribe(onNext, onError, onComplete);
        else if (currentClass.equals(ObservableSubscribeOn.class))
            observable.subscribe(onNext, onError, onComplete);
        else if (currentClass.equals(MaybeSubscribeOn.class))
            maybe.subscribe(onNext, onError, onComplete);
        else if (currentClass.equals(SingleSubscribeOn.class))
            throw new SingleNotPermitedException();
        else if (currentClass.equals(ParallelMap.class))
            parallelFlowable.sequential().subscribe(onNext, onError, onComplete);
    }

    public void subscribe(Consumer onNext, Consumer onError) {
        if (currentClass.equals(FlowableSubscribeOn.class))
            flowable.subscribe(onNext, onError);
        else if (currentClass.equals(ObservableSubscribeOn.class))
            observable.subscribe(onNext, onError);
        else if (currentClass.equals(MaybeSubscribeOn.class))
            maybe.subscribe(onNext, onError);
        else if (currentClass.equals(SingleSubscribeOn.class))
            single.subscribe(onNext, onError);
        else if (currentClass.equals(ParallelMap.class))
            parallelFlowable.sequential().subscribe(onNext, onError);
    }

}
