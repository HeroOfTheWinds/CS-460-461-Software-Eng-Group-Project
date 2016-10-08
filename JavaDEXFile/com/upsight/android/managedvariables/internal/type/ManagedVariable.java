package com.upsight.android.managedvariables.internal.type;

import java.util.Observable;

public abstract class ManagedVariable<T> extends Observable {
    private T mDefaultValue;
    private String mTag;
    private T mValue;

    protected ManagedVariable(String str, T t, T t2) {
        Object obj;
        this.mTag = str;
        this.mDefaultValue = t;
        if (t2 != null) {
            obj = t2;
        }
        this.mValue = obj;
    }

    public T get() {
        T t;
        synchronized (this) {
            t = this.mValue;
        }
        return t;
    }

    public String getTag() {
        return this.mTag;
    }

    void set(T t) {
        synchronized (this) {
            if (t != this.mValue) {
                if (t != null) {
                    this.mValue = t;
                } else {
                    this.mValue = this.mDefaultValue;
                }
                setChanged();
                notifyObservers();
            }
        }
    }
}
