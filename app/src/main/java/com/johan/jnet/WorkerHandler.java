package com.johan.jnet;

import android.os.Handler;

import com.johan.jnet.http.Response;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2016/10/9.
 */
public class WorkerHandler extends Handler {

    private static final ThreadLocal<WeakReference<WorkerHandler>> handlers = new ThreadLocal<>();

    public static WorkerHandler workerHandler() {
        final WeakReference<WorkerHandler> handlerReference = handlers.get();
        WorkerHandler handler = handlerReference != null ? handlerReference.get() : null;
        if (handler == null) {
            handler = new WorkerHandler();
            handlers.set(new WeakReference<>(handler));
        }
        return handler;
    }

    public void workResponse(final com.johan.jnet.http.Callback callback, final Response response) {
        if (callback == null) return;
        post(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(response);
            }
        });
    }

    public void workFailure(final com.johan.jnet.http.Callback callback, final String reason) {
        if (callback == null) return;
        post(new Runnable() {
            @Override
            public void run() {
                callback.onFailure(reason);
            }
        });
    }

}
