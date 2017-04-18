package com.canaban.handler;

import com.canaban.subscriber.DeferredSubscriber;
import org.springframework.web.method.support.AsyncHandlerMethodReturnValueHandler;

/**
 * Created by antongusev on 28.03.17.
 */
public interface DefaultHandler extends AsyncHandlerMethodReturnValueHandler {

    DeferredSubscriber getDeferredSubscriber();

}
