package com.squareup.otto;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class Bus {
    public static final String DEFAULT_IDENTIFIER = "default";
    private final ThreadEnforcer enforcer;
    private final ThreadLocal<ConcurrentLinkedQueue<EventWithHandler>> eventsToDispatch;
    private final ConcurrentMap<Class<?>, Set<Class<?>>> flattenHierarchyCache;
    private final HandlerFinder handlerFinder;
    private final ConcurrentMap<Class<?>, Set<EventHandler>> handlersByType;
    private final String identifier;
    private final ThreadLocal<Boolean> isDispatching;
    private final ConcurrentMap<Class<?>, EventProducer> producersByType;

    /* renamed from: com.squareup.otto.Bus.1 */
    class C07831 extends ThreadLocal<ConcurrentLinkedQueue<EventWithHandler>> {
        C07831() {
        }

        protected ConcurrentLinkedQueue<EventWithHandler> initialValue() {
            return new ConcurrentLinkedQueue();
        }
    }

    /* renamed from: com.squareup.otto.Bus.2 */
    class C07842 extends ThreadLocal<Boolean> {
        C07842() {
        }

        protected Boolean initialValue() {
            return Boolean.valueOf(false);
        }
    }

    static class EventWithHandler {
        final Object event;
        final EventHandler handler;

        public EventWithHandler(Object obj, EventHandler eventHandler) {
            this.event = obj;
            this.handler = eventHandler;
        }
    }

    public Bus() {
        this(DEFAULT_IDENTIFIER);
    }

    public Bus(ThreadEnforcer threadEnforcer) {
        this(threadEnforcer, DEFAULT_IDENTIFIER);
    }

    public Bus(ThreadEnforcer threadEnforcer, String str) {
        this(threadEnforcer, str, HandlerFinder.ANNOTATED);
    }

    Bus(ThreadEnforcer threadEnforcer, String str, HandlerFinder handlerFinder) {
        this.handlersByType = new ConcurrentHashMap();
        this.producersByType = new ConcurrentHashMap();
        this.eventsToDispatch = new C07831();
        this.isDispatching = new C07842();
        this.flattenHierarchyCache = new ConcurrentHashMap();
        this.enforcer = threadEnforcer;
        this.identifier = str;
        this.handlerFinder = handlerFinder;
    }

    public Bus(String str) {
        this(ThreadEnforcer.MAIN, str);
    }

    private void dispatchProducerResultToHandler(EventHandler eventHandler, EventProducer eventProducer) {
        Object obj = null;
        try {
            obj = eventProducer.produceEvent();
        } catch (InvocationTargetException e) {
            throwRuntimeException("Producer " + eventProducer + " threw an exception.", e);
        }
        if (obj != null) {
            dispatch(obj, eventHandler);
        }
    }

    private Set<Class<?>> getClassesFor(Class<?> cls) {
        List linkedList = new LinkedList();
        Set<Class<?>> hashSet = new HashSet();
        linkedList.add(cls);
        while (!linkedList.isEmpty()) {
            Class cls2 = (Class) linkedList.remove(0);
            hashSet.add(cls2);
            cls2 = cls2.getSuperclass();
            if (cls2 != null) {
                linkedList.add(cls2);
            }
        }
        return hashSet;
    }

    private static void throwRuntimeException(String str, InvocationTargetException invocationTargetException) {
        Throwable cause = invocationTargetException.getCause();
        if (cause != null) {
            throw new RuntimeException(str + ": " + cause.getMessage(), cause);
        }
        throw new RuntimeException(str + ": " + invocationTargetException.getMessage(), invocationTargetException);
    }

    protected void dispatch(Object obj, EventHandler eventHandler) {
        try {
            eventHandler.handleEvent(obj);
        } catch (InvocationTargetException e) {
            throwRuntimeException("Could not dispatch event: " + obj.getClass() + " to handler " + eventHandler, e);
        }
    }

    protected void dispatchQueuedEvents() {
        if (!((Boolean) this.isDispatching.get()).booleanValue()) {
            this.isDispatching.set(Boolean.valueOf(true));
            while (true) {
                EventWithHandler eventWithHandler = (EventWithHandler) ((ConcurrentLinkedQueue) this.eventsToDispatch.get()).poll();
                if (eventWithHandler == null) {
                    break;
                }
                try {
                    if (eventWithHandler.handler.isValid()) {
                        dispatch(eventWithHandler.event, eventWithHandler.handler);
                    }
                } finally {
                    this.isDispatching.set(Boolean.valueOf(false));
                }
            }
        }
    }

    protected void enqueueEvent(Object obj, EventHandler eventHandler) {
        ((ConcurrentLinkedQueue) this.eventsToDispatch.get()).offer(new EventWithHandler(obj, eventHandler));
    }

    Set<Class<?>> flattenHierarchy(Class<?> cls) {
        Set<Class<?>> set = (Set) this.flattenHierarchyCache.get(cls);
        if (set != null) {
            return set;
        }
        Set<Class<?>> classesFor = getClassesFor(cls);
        set = (Set) this.flattenHierarchyCache.putIfAbsent(cls, classesFor);
        return set == null ? classesFor : set;
    }

    Set<EventHandler> getHandlersForEventType(Class<?> cls) {
        return (Set) this.handlersByType.get(cls);
    }

    EventProducer getProducerForEventType(Class<?> cls) {
        return (EventProducer) this.producersByType.get(cls);
    }

    public void post(Object obj) {
        if (obj == null) {
            throw new NullPointerException("Event to post must not be null.");
        }
        this.enforcer.enforce(this);
        Object obj2 = null;
        for (Class handlersForEventType : flattenHierarchy(obj.getClass())) {
            Set<EventHandler> handlersForEventType2 = getHandlersForEventType(handlersForEventType);
            if (!(handlersForEventType2 == null || handlersForEventType2.isEmpty())) {
                obj2 = 1;
                for (EventHandler enqueueEvent : handlersForEventType2) {
                    enqueueEvent(obj, enqueueEvent);
                }
            }
        }
        if (obj2 == null && !(obj instanceof DeadEvent)) {
            post(new DeadEvent(this, obj));
        }
        dispatchQueuedEvents();
    }

    public void register(Object obj) {
        if (obj == null) {
            throw new NullPointerException("Object to register must not be null.");
        }
        this.enforcer.enforce(this);
        Map findAllProducers = this.handlerFinder.findAllProducers(obj);
        for (Class cls : findAllProducers.keySet()) {
            EventProducer eventProducer = (EventProducer) findAllProducers.get(cls);
            EventProducer eventProducer2 = (EventProducer) this.producersByType.putIfAbsent(cls, eventProducer);
            if (eventProducer2 != null) {
                throw new IllegalArgumentException("Producer method for type " + cls + " found on type " + eventProducer.target.getClass() + ", but already registered by type " + eventProducer2.target.getClass() + ".");
            }
            Set<EventHandler> set = (Set) this.handlersByType.get(cls);
            if (!(set == null || set.isEmpty())) {
                for (EventHandler dispatchProducerResultToHandler : set) {
                    dispatchProducerResultToHandler(dispatchProducerResultToHandler, eventProducer);
                }
            }
        }
        findAllProducers = this.handlerFinder.findAllSubscribers(obj);
        for (Class cls2 : findAllProducers.keySet()) {
            Set set2 = (Set) this.handlersByType.get(cls2);
            if (set2 == null) {
                CopyOnWriteArraySet copyOnWriteArraySet = new CopyOnWriteArraySet();
                set2 = (Set) this.handlersByType.putIfAbsent(cls2, copyOnWriteArraySet);
                if (set2 == null) {
                    set2 = copyOnWriteArraySet;
                }
            }
            if (!set2.addAll((Set) findAllProducers.get(cls2))) {
                throw new IllegalArgumentException("Object already registered.");
            }
        }
        for (Entry entry : findAllProducers.entrySet()) {
            eventProducer = (EventProducer) this.producersByType.get((Class) entry.getKey());
            if (eventProducer != null && eventProducer.isValid()) {
                for (EventHandler dispatchProducerResultToHandler2 : (Set) entry.getValue()) {
                    if (!eventProducer.isValid()) {
                        break;
                    } else if (dispatchProducerResultToHandler2.isValid()) {
                        dispatchProducerResultToHandler(dispatchProducerResultToHandler2, eventProducer);
                    }
                }
            }
        }
    }

    public String toString() {
        return "[Bus \"" + this.identifier + "\"]";
    }

    public void unregister(Object obj) {
        if (obj == null) {
            throw new NullPointerException("Object to unregister must not be null.");
        }
        this.enforcer.enforce(this);
        for (Entry entry : this.handlerFinder.findAllProducers(obj).entrySet()) {
            Class cls = (Class) entry.getKey();
            EventProducer producerForEventType = getProducerForEventType(cls);
            EventProducer eventProducer = (EventProducer) entry.getValue();
            if (eventProducer == null || !eventProducer.equals(producerForEventType)) {
                throw new IllegalArgumentException("Missing event producer for an annotated method. Is " + obj.getClass() + " registered?");
            }
            ((EventProducer) this.producersByType.remove(cls)).invalidate();
        }
        for (Entry entry2 : this.handlerFinder.findAllSubscribers(obj).entrySet()) {
            Set<EventHandler> handlersForEventType = getHandlersForEventType((Class) entry2.getKey());
            Collection collection = (Collection) entry2.getValue();
            if (handlersForEventType == null || !handlersForEventType.containsAll(collection)) {
                throw new IllegalArgumentException("Missing event handler for an annotated method. Is " + obj.getClass() + " registered?");
            }
            for (EventHandler eventHandler : handlersForEventType) {
                if (collection.contains(eventHandler)) {
                    eventHandler.invalidate();
                }
            }
            handlersForEventType.removeAll(collection);
        }
    }
}
