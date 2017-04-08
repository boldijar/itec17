package com.bolnizar.code.presenter;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class BasePresenter<T> {

    private T mView;
    private CompositeSubscription mCompositeSubscription;

    public void init(T view) {
        mView = view;
        mCompositeSubscription = new CompositeSubscription();
    }

    protected final T getView() {
        if (mView == null) {
            throw new RuntimeException("You must call init()");
        }
        return mView;
    }

    protected final void addSubscription(Subscription subscription) {
        if (mView == null) {
            throw new RuntimeException("You must call init()");
        }
        mCompositeSubscription.add(subscription);
    }

    public void destroy() {
        mCompositeSubscription.clear();
    }

}
