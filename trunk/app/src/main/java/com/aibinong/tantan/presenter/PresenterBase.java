package com.aibinong.tantan.presenter;

import com.fatalsignal.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import rx.Subscription;

/**
 * Created by yourfriendyang on 16/6/28.
 */
public abstract class PresenterBase {
    private ArrayList<WeakReference<Subscription>> subScriptionRefs;

    public void onCreate() {
    }

    public void onDestoryView() {
        Log.i(this.getClass().getSimpleName() + ":", "onDestoryView");
        if (subScriptionRefs != null) {
            for (int i = 0; i < subScriptionRefs.size(); i++) {
                WeakReference<Subscription> subRef = subScriptionRefs.get(i);
                if (subRef != null) {
                    Subscription subscription = subRef.get();
                    if (subscription != null && !subscription.isUnsubscribed()) {
                        subscription.unsubscribe();
                    }
                }
            }
            subScriptionRefs.clear();
        }
    }

    public Subscription addToCycle(Subscription subscription) {
        if (subScriptionRefs == null) {
            subScriptionRefs = new ArrayList<>();
        }
        subScriptionRefs.add(new WeakReference<Subscription>(subscription));
        return subscription;
    }
}
