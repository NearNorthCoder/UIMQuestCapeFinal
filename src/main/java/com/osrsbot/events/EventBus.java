package com.osrsbot.events;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Simple event bus for publish/subscribe of events.
 */
public class EventBus {
    private static final Map<Class<?>, List<Consumer<?>>> listeners = new ConcurrentHashMap<>();

    public static <T> void subscribe(Class<T> eventType, Consumer<T> handler) {
        listeners.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(handler);
    }

    public static <T> void unsubscribe(Class<T> eventType, Consumer<T> handler) {
        List<Consumer<?>> handlers = listeners.get(eventType);
        if (handlers != null) {
            handlers.remove(handler);
        }
    }

    public static <T> void publish(T event) {
        List<Consumer<?>> handlers = listeners.get(event.getClass());
        if (handlers != null) {
            for (Consumer<?> h : handlers) {
                @SuppressWarnings("unchecked")
                Consumer<T> handler = (Consumer<T>) h;
                handler.accept(event);
            }
        }
    }
}