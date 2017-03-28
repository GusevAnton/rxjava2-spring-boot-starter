package com.canaban.handler;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.parallel.ParallelFlowable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by antongusev on 18.03.17.
 */
@Slf4j
@RestController
public class TestController {

//    @GetMapping
//    public Flowable<String> test() {
//        return Flowable.fromCallable(() -> {
//            throw new BadRequestException();
//        });
//    }

    @GetMapping("/flowable")
    public Flowable flowable() {
        return Flowable.fromCallable(() -> 1000).map(i -> i * i);
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

//    @Emmiter
//    @GetMapping("/test2")
//    public Flowable<Integer> test2() {
//        return Flowable.fromCallable(() -> IntStream.range(0, 100).boxed().collect(Collectors.toList()))
//                .map(res -> {throw new BadRequestException();});
//    }

//    @Emmiter
    @GetMapping("/test3")
    public ParallelFlowable<Integer> parallelTest2() {
        return Flowable.fromCallable(() -> IntStream.range(0, 100).boxed().collect(Collectors.toList()))
                .flatMap(Flowable::fromIterable)
                .parallel().map(i -> i*i).doOnNext(res -> log.info("{}", res));
    }

}
