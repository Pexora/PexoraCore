package de.pexora.core.api.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

/**
 * Hilfsklasse zur Registrierung von Event-Listenern im PexoraCore-System.
 * Diese Klasse vereinfacht die Registrierung von Event-Handlern.
 */
public class EventRegistration {
    
    private final Plugin plugin;
    
    /**
     * Erstellt eine neue EventRegistration-Instanz
     * 
     * @param plugin Das Plugin, für das Events registriert werden sollen
     */
    public EventRegistration(Plugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Registriert einen Listener für alle seine Event-Handler
     * 
     * @param listener Der zu registrierende Listener
     */
    public void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }
    
    /**
     * Registriert mehrere Listener auf einmal
     * 
     * @param listeners Die zu registrierenden Listener
     */
    public void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            registerListener(listener);
        }
    }
    
    /**
     * Hebt die Registrierung aller Listener eines Plugins auf
     */
    public void unregisterAllListeners() {
        Bukkit.getPluginManager().registerEvents(new Listener() {}, plugin);
    }
    
    /**
     * Registriert einen spezifischen Event-Handler für einen bestimmten Event-Typ
     * 
     * @param <T> Der Event-Typ
     * @param eventClass Die Event-Klasse
     * @param handler Der Event-Handler
     * @param priority Die Event-Priorität
     * @param ignoreCancelled Ob abgebrochene Events ignoriert werden sollen
     */
    public <T extends Event> void registerEvent(Class<T> eventClass, EventHandler<T> handler, 
                                               EventPriority priority, boolean ignoreCancelled) {
        Bukkit.getPluginManager().registerEvent(
            eventClass,
            new Listener() {},
            priority,
            (listener, event) -> {
                if (eventClass.isInstance(event)) {
                    handler.handle(eventClass.cast(event));
                }
            },
            plugin,
            ignoreCancelled
        );
    }
    
    /**
     * Registriert einen spezifischen Event-Handler mit normaler Priorität
     * 
     * @param <T> Der Event-Typ
     * @param eventClass Die Event-Klasse
     * @param handler Der Event-Handler
     */
    public <T extends Event> void registerEvent(Class<T> eventClass, EventHandler<T> handler) {
        registerEvent(eventClass, handler, EventPriority.NORMAL, false);
    }
    
    /**
     * Registriert einen spezifischen Event-Handler mit bestimmter Priorität
     * 
     * @param <T> Der Event-Typ
     * @param eventClass Die Event-Klasse
     * @param handler Der Event-Handler
     * @param priority Die Event-Priorität
     */
    public <T extends Event> void registerEvent(Class<T> eventClass, EventHandler<T> handler, 
                                               EventPriority priority) {
        registerEvent(eventClass, handler, priority, false);
    }
    
    /**
     * Funktionales Interface für Event-Handler
     * 
     * @param <T> Der Event-Typ
     */
    @FunctionalInterface
    public interface EventHandler<T extends Event> {
        void handle(T event);
    }
}