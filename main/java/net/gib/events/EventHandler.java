package net.gib.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class EventHandler {

    private final Listener[] listeners;
    private Parameter eObj;


    public EventHandler(Listener... listeners) {
        this.listeners = listeners;
    }

    public Listener[] getListeners() {
        return listeners;
    }


    public  <T extends Event> void fireEvent(T event) {

        for (Listener listener : listeners) {
            for (Method method : listener.getClass().getMethods()) {
                if (!method.isAnnotationPresent(ActionMethod.class)) continue;
                if (method.getParameterCount() != 1) continue;
                var eObj = method.getParameterTypes()[0];
                if (!eObj.getName().equals(event.getClass().getName())) continue;
                try {
                    method.invoke(listener,event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

}
