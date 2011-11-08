package org.projecthdata.viewer.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import android.app.Application;

/**
 * Inspired by eventbus.org's library. This is a lightweight version originally
 * intended to allow objects created by SimpleXML to publish themselves at
 * creation time so they can be inserted into the database.
 * 
 * @author elevine
 * 
 */
public class EventBus {

	@SuppressWarnings("rawtypes")
	private static Map<Class, Method> CALLBACKS = new HashMap<Class, Method>();
	private static Object SUBSCRIBER = null;

	public static void registerSubscriber(Object subscriber) {
		detachCurrentSubscriber();
		SUBSCRIBER = subscriber;
		for (Method method : subscriber.getClass().getDeclaredMethods()) {
			if (method.isAnnotationPresent(EventSubscriber.class)) {

				EventSubscriber eventSubscriber = method
						.getAnnotation(EventSubscriber.class);
				CALLBACKS.put(eventSubscriber.value(), method);
			}
		}
	}

	public static void detachCurrentSubscriber() {
		SUBSCRIBER = null;
		CALLBACKS.clear();
	}

	public static void publish(Object event) {
		try {
			Method callback = CALLBACKS.get(event.getClass());
			if ((callback != null) && (SUBSCRIBER != null)) {
				callback.invoke(SUBSCRIBER, event);
			}
		} catch (IllegalArgumentException e) {
			throwRuntimeException(e, event);
		} catch (IllegalAccessException e) {
			throwRuntimeException(e, event);
		} catch (InvocationTargetException e) {
			throwRuntimeException(e, event);
		}

	}

	private static void throwRuntimeException(Exception e, Object event) {
		String message = "There was an EventBus error trying to invoke a callback method registered for "
				+ event.getClass().getSimpleName();
		throw new RuntimeException(message, e);
	}

}
