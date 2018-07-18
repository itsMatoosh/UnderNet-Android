package me.matoosh.undernet.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Manages the events generated by the application.
 * Created by Mateusz Rębacz on 21.02.2017.
 */

public class EventManager {
    /**
     * List of all the registered event handlers.
     */
    public static HashMap<Class, ArrayList<EventHandler>> eventHandlers = new HashMap<>();

    private static Logger logger = LoggerFactory.getLogger(EventManager.class);

    /**
     * Registers an event.
     * @param eventType
     */
    public static void registerEvent(Class eventType) {
        if (!eventHandlers.containsKey(eventType)) {
            logger.debug("Registered event type: {}", eventType.toString());
            eventHandlers.put(eventType, new ArrayList<EventHandler>());
        } else {
            logger.debug("Event type: {}, already registered!", eventType.toString());
        }
    }

    /**
     * Registers an event handler.
     * @param handler
     * @param eventType
     */
    public static void registerHandler(EventHandler handler, Class eventType) {
        if(!eventHandlers.containsKey(eventType)) {
            registerEvent(eventType);
        }

        eventHandlers.get(eventType).add(handler);
    }

    /**
     * Unregisters an event handler.
     * @param handler
     * @param eventType
     */
    public static void unregisterHandler(EventHandler handler, Class eventType) {
        if(!eventHandlers.containsKey(eventType)) {
            registerEvent(eventType);
            return;
        }
        eventHandlers.get(eventType).remove(handler);
    }

    /**
     * Calls the specified event.
     * @param event
     */
    public static void callEvent(Event event) {
        ArrayList<EventHandler> handlers = eventHandlers.get(event.getClass());
        if(handlers == null) {
            logger.warn("Event type: {} hasn't been registered!", event.getClass());
            return;
        }
        event.onCalled();
        for (int i = 0; i < handlers.size(); i++) {
            if(handlers.get(i) == null) {
                continue;
            }
            try {
                handlers.get(i).onEventCalled(event);
            } catch (Exception e) {
                logger.error("Error while running event handler!", e);
                continue;
            }
        }
    }
}
