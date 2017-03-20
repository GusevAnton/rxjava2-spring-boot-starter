package com.canaban.handler;

import org.springframework.web.context.request.async.DeferredResult;

/**
 * Created by antongusev on 18.03.17.
 */
public interface ExceptionHandler {

    void handleException(DeferredResult deferredResult, Throwable throwable);

}
