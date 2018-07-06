package com.arny.celestiatools.utils;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class RxUtils {
    public static <T> Observable<T> IOThreadObservable(Observable<T> observable) {
        return observable.subscribeOn(Schedulers.io());
    }

    public static <T> Observable<T> CompThreadObservable(Observable<T> observable) {
        return observable.subscribeOn(Schedulers.computation());
    }

    public static <T> Observable<T> IOThreadObservable(Scheduler scheduler, Observable<T> observable) {
        return observable.subscribeOn(scheduler);
    }
}
