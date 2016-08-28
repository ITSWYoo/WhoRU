package com.yoo.ymh.whoru.util;

import com.yoo.ymh.whoru.model.Contact;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * courtesy: https://gist.github.com/benjchristensen/04eef9ca0851f3a5d7bf
 */
public class RxBus {

    //private final PublishSubject<Object> _bus = PublishSubject.create();

    // If multiple threads are going to emit events to this
    // then it must be made thread-safe like this instead
    private static final RxBus INSTANCE =new RxBus();

    private final Subject<Object, Object> _bus = new SerializedSubject<>(PublishSubject.create());//publishSubject는 구독 후 모든 item Subscribe가능

    public static RxBus getInstance(){

        return INSTANCE;
    }
    public void send(Object o) {
        _bus.onNext(o);
    }

    public Observable<Object> toObserverable() {
        return _bus;
    }

    public boolean hasObservers() {
        return _bus.hasObservers();
    }
}