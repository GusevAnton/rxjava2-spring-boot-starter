package com.canaban.exception;

import com.canaban.dto.Error;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Created by antongusev on 18.03.17.
 */
@Component
@ConditionalOnBean(ExceptionHandler.class)
public class ExceptionHandlerImpl implements ExceptionHandler {

    public void handleException(DeferredResult deferredResult, Throwable throwable) {
        Class clazz = throwable.getClass();
        ResponseStatus annotation = (ResponseStatus) clazz.getAnnotation(ResponseStatus.class);
        deferredResult.setErrorResult(annotation == null ? ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error(throwable.getMessage())) : ResponseEntity.status(annotation.code()).body(new Error(annotation.reason())));
    }
}
