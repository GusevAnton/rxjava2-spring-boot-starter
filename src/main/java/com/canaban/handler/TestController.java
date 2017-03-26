package com.canaban.handler;

import com.canaban.config.Emmiter;
import io.reactivex.Flowable;
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

    @GetMapping("/test")
    public Flowable test1() {
        return Flowable.fromCallable(() -> 1000).map(i -> i * i);
    }

//    @Emmiter
//    @GetMapping("/test2")
//    public Flowable<Integer> test2() {
//        return Flowable.fromCallable(() -> IntStream.range(0, 100).boxed().collect(Collectors.toList()))
//                .map(res -> {throw new BadRequestException();});
//    }

    @Emmiter
    @GetMapping("/test3")
    public ParallelFlowable<Integer> parallelTest2() {
        return Flowable.fromCallable(() -> IntStream.range(0, 100).boxed().collect(Collectors.toList()))
                .flatMap(Flowable::fromIterable)
                .parallel().map(i -> i*i).doOnNext(res -> log.info("{}", res));
    }

}
