package de.pexora.core.api.status;

import java.util.Map;

/**
 * API zum Verfolgen und Abfragen des Status aller Module im System.
 * Diese Schnittstelle ermöglicht den Zugriff auf den Modulstatus ohne direkte Abhängigkeiten zur Core-Implementierung.
 */
public interface StatusAPI {
    
    /**
     * Registriert ein Modul mit seinem Aktivierungsstatus
     * 
     * @param moduleName Der Name des Moduls
     * @param enabled Ob das Modul aktiviert ist
     */
    void registerModule(String moduleName, boolean enabled);
    
    /**
     * Hebt die Registrierung eines Moduls auf
     * 
     * @param moduleName Der Name des Moduls
     */
    void unregisterModule(String moduleName);
    
    /**
     * Prüft, ob ein Modul aktiviert ist
     * 
     * @param moduleName Der Name des Moduls
     * @return Ob das Modul aktiviert ist, oder false wenn nicht gefunden
     */
    boolean isModuleEnabled(String moduleName);
    
    /**
     * Gibt den Status aller Module zurück
     * 
     * @return Eine Map mit Modulnamen und ihrem Aktivierungsstatus
     */
    Map<String, Boolean> getModuleStatus();
    
    /**
     * Gibt die Anzahl der aktivierten Module zurück
     * 
     * @return Die Anzahl der aktivierten Module
     */
    int getEnabledModuleCount();
    
    /**
     * Gibt die Gesamtanzahl der registrierten Module zurück
     * 
     * @return Die Gesamtanzahl der Module
     */
    int getTotalModuleCount();
    
    /**
     * Gibt ein ModuleStatus-Objekt für ein bestimmtes Modul zurück
     * 
     * @param moduleName Der Name des Moduls
     * @return Das ModuleStatus-Objekt oder null, wenn nicht gefunden
     */
    ModuleStatus getModuleStatusForModule(String moduleName);
}