package com.rahul;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
	    hello("Iron Man", "The Hulk", "Thor");

	    String[] stringArray = new String[]{"Namaste", "Hello", "Bonjour"};
	    Observable<String> observable = Observable.fromArray(stringArray);

        List<Integer> integerList = Arrays.asList(new Integer[]{1,2,3,4,5,6,7,8,9,10});
        Observable<Integer> integerObservable = Observable.fromIterable(integerList);

        Observable<String> stringObservable = Observable.just("Just One String");

        // Custom Observable
        Observable.create(emitter -> {
            try {
                // Emit a stream of values 0 - 10
                IntStream.range(0, 10).boxed().forEach(integer ->
                        System.out.println("integer = " + integer)
                );
                emitter.onComplete();
            }
            catch (Exception e) {
                emitter.onError(e);
            }
        });

        Observable interval = Observable.interval(100, TimeUnit.MILLISECONDS);

        // Consuming observables
        integerObservable.subscribe(intVal -> System.out.println(intVal + " "));
        System.out.println("Using static method reference");
        integerObservable.subscribe(Main::printObservable);

        // Filter out values than aren't greater than 4
        System.out.println("\nAfter Filter:");
        integerObservable.filter(integer -> integer > 4).subscribe(Main::printObservable);

        // Map each value to the square of that value
        System.out.println("\nAfter Map:");
        integerObservable.map(integer -> Math.multiplyExact(integer, integer)).subscribe(Main::printObservable);

        //Custom Observer
        System.out.println("\nUsing ConsolePrintObserver");
        integerObservable.subscribe(new ConsolePrintObserver());

        // Using new thread scheduler
        System.out.println("Using new thread scheduler");
        integerObservable.unsubscribeOn(Schedulers.newThread()).subscribe(new ConsolePrintObserver());

        // Use it all in one statement
        System.out.println("Bring it all together");
        integerObservable
                .filter(v -> v > 4)
                .map(v -> Math.multiplyExact(v, v))
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(new ConsolePrintObserver());

    }

    private static <T>void printObservable(T val) {
        System.out.print(val + "-");
    }

    public static void hello(String... names) {
        Observable.fromArray(names).subscribe(s -> System.out.println("Hello " + s));
    }
}
