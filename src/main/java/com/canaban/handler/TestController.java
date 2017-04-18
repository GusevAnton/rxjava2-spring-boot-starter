package com.canaban.handler;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

/**
 * Created by antongusev on 18.03.17.
 */
@Slf4j
@Controller
public class TestController {

    @GetMapping("/flowable")
    public Single flowable() {
        return Flowable.range(1, 100000)
                .map(i -> i * i).doOnNext((v) -> log.info("zozozozo")).reduce(new ArrayList(), (list, value) -> {
                    list.add(value);
                    return list;
                });
    }

    @GetMapping("/single")
    public Single single() {
        return Single.fromCallable(() -> 1000).map(i -> i * i);
    }

    @GetMapping("/maybe")
    public Maybe maybe() {
        return Maybe.fromCallable(() -> 1000).map(i -> i * i);
    }

    @GetMapping("/observable")
    public Observable observable() {
        return Observable.fromCallable(() -> 1000).map(i -> i * i);
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String greeting() throws Exception {
        Thread.sleep(1000);
        return new String("1234567890");
    }

}
