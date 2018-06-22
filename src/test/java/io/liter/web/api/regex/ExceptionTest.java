package io.liter.web.api.regex;


import java.util.Random;

import org.junit.Test;
import org.reactivestreams.Subscription;

import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

public class ExceptionTest {


    @Test
    public void runTest()

    {

        Random RANDOM = new Random();

        Flux<String> flux = Flux
//								.<String>error(new IllegalArgumentException());
                .range(1, 4)
                .map(item -> {
                    if (item <= 3)
                        return "item: " + item;
                    else {
                        System.out.println(">> Exception occurs on map()");
                        throw new RuntimeException();
                    }
                });

        System.out.println("=== do when error ===");
        flux.doOnError(e -> System.out.println("doOnError: " + e))
                .subscribe(System.out::println);

        System.out.println("=== fall back to a default value ===");
        flux.onErrorReturn("onErrorReturn: Value!")
                .subscribe(System.out::println);

        System.out.println("=== fall back to another Flux ===");

        flux.onErrorResume(e -> {
            System.out.println("-> inside onErrorResumeWith()");
            return Flux.just(1, 2)
                    .map(item -> {
                        return "-> new Flux item: " + item;
                    });
        })
                .subscribe(System.out::println);

        System.out.println("=== retry ===");
        flux.retry(1)
                .doOnError(System.out::println)
                .subscribe(System.out::println);

        System.out.println("=== retry with Predicate ===");
        flux.retry(1, e -> {
            boolean shouldRetry = RANDOM.nextBoolean();
            System.out.println("shouldRetry? -> " + shouldRetry);
            return shouldRetry;
        })
                .doOnError(System.out::println)
                .subscribe(System.out::println);

        System.out.println("=== deal with backpressure Error ===");
        flux.onBackpressureError()
                .doOnError(System.out::println)
                .subscribe(new BaseSubscriber<String>() {

                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        System.out.println("Subscriber > request only 1 item...");
                        request(1);
                    }

                    @Override
                    protected void hookOnNext(String value) {
                        System.out.println("Subscriber > process... [" + value + "]");
                    }
                });

        System.out.println("===  dropping excess values ===");
        flux.onBackpressureDrop(item -> System.out.println("Drop: [" + item + "]"))
                .doOnError(System.out::println)
                .subscribe(new BaseSubscriber<String>() {

                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        System.out.println("Subscriber > request only 1 item...");
                        request(1);
                    }

                    @Override
                    protected void hookOnNext(String value) {
                        System.out.println("Subscriber > process... [" + value + "]");
                    }
                });

        System.out.println("===  buffer excess values ===");
        BaseSubscriber<String> subscriber = new BaseSubscriber<String>() {

            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                System.out.println("Subscriber > request only 1 item...");
                request(1);
            }

            @Override
            protected void hookOnNext(String value) {
                System.out.println("Subscriber > process... [" + value + "]");
            }
        };

        flux.onBackpressureBuffer(2, item -> System.out.println("Buffer: [" + item + "]"))
                .doOnError(System.out::println)
                .subscribe(subscriber);

        System.out.println("Subscriber > request more items:");
        subscriber.request(1);

        System.out.println("=== catch and rethrow ===");
        /*flux.mapError(e -> new CustomException("mapError"))
                .subscribe(System.out::println);*/
    }
}
