package com.mopub.volley.toolbox;

import com.mopub.volley.Request;
import com.mopub.volley.Response.ErrorListener;
import com.mopub.volley.Response.Listener;
import com.mopub.volley.VolleyError;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RequestFuture<T> implements Future<T>, Listener<T>, ErrorListener {
    private VolleyError mException;
    private Request<?> mRequest;
    private T mResult;
    private boolean mResultReceived;

    private RequestFuture() {
        this.mResultReceived = false;
    }

    private T doGet(Long l) throws InterruptedException, ExecutionException, TimeoutException {
        T t;
        synchronized (this) {
            if (this.mException != null) {
                throw new ExecutionException(this.mException);
            }
            if (this.mResultReceived) {
                t = this.mResult;
            } else {
                if (l == null) {
                    wait(0);
                } else if (l.longValue() > 0) {
                    wait(l.longValue());
                }
                if (this.mException != null) {
                    throw new ExecutionException(this.mException);
                } else if (this.mResultReceived) {
                    t = this.mResult;
                } else {
                    throw new TimeoutException();
                }
            }
        }
        return t;
    }

    public static <E> RequestFuture<E> newFuture() {
        return new RequestFuture();
    }

    public boolean cancel(boolean z) {
        boolean z2 = false;
        synchronized (this) {
            if (this.mRequest != null) {
                if (!isDone()) {
                    this.mRequest.cancel();
                    z2 = true;
                }
            }
        }
        return z2;
    }

    public T get() throws InterruptedException, ExecutionException {
        try {
            return doGet(null);
        } catch (TimeoutException e) {
            throw new AssertionError(e);
        }
    }

    public T get(long j, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        return doGet(Long.valueOf(TimeUnit.MILLISECONDS.convert(j, timeUnit)));
    }

    public boolean isCancelled() {
        return this.mRequest == null ? false : this.mRequest.isCanceled();
    }

    public boolean isDone() {
        boolean z;
        synchronized (this) {
            z = this.mResultReceived || this.mException != null || isCancelled();
        }
        return z;
    }

    public void onErrorResponse(VolleyError volleyError) {
        synchronized (this) {
            this.mException = volleyError;
            notifyAll();
        }
    }

    public void onResponse(T t) {
        synchronized (this) {
            this.mResultReceived = true;
            this.mResult = t;
            notifyAll();
        }
    }

    public void setRequest(Request<?> request) {
        this.mRequest = request;
    }
}
