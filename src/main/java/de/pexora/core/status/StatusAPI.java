package de.pexora.core.status;

import de.pexora.core.api.status.ModuleStatus;
import java.util.HashMap;
import java.util.Map;

/**
 * Verfolgt den Status aller Module im System.
 * Implementiert das StatusAPI-Interface aus dem API-Paket.
 */
public class StatusAPI implements de.pexora.core.api.status.StatusAPI {

    private final Map<String, Boolean> moduleStatus;
    private final Map<String, ModuleStatus> moduleStatusObjects;
    
    public StatusAPI() {
        this.moduleStatus = new HashMap<>();
        this.moduleStatusObjects = new HashMap<>();
    }
    
    /**
     * Registriert ein Modul mit seinem Aktivierungsstatus
     * 
     * @param moduleName Der Name des Moduls
     * @param enabled Ob das Modul aktiviert ist
     */
    @Override
    public void registerModule(String moduleName, boolean enabled) {
        moduleStatus.put(moduleName, enabled);
        moduleStatusObjects.put(moduleName, new ModuleStatus(moduleName, enabled));
    }
    
    /**
     * Hebt die Registrierung eines Moduls auf
     * 
     * @param moduleName Der Name des Moduls
     */
    @Override
    public void unregisterModule(String moduleName) {
        moduleStatus.remove(moduleName);
        moduleStatusObjects.remove(moduleName);
    }
    
    /**
     * Prüft, ob ein Modul aktiviert ist
     * 
     * @param moduleName Der Name des Moduls
     * @return Ob das Modul aktiviert ist, oder false wenn nicht gefunden
     */
    @Override
    public boolean isModuleEnabled(String moduleName) {
        return moduleStatus.getOrDefault(moduleName, false);
    }
    
    /**
     * Gibt den Status aller Module zurück
     * 
     * @return Eine Map mit Modulnamen und ihrem Aktivierungsstatus
     */
    @Override
    public Map<String, Boolean> getModuleStatus() {
        return new HashMap<>(moduleStatus);
    }
    
    /**
     * Gibt die Anzahl der aktivierten Module zurück
     * 
     * @return Die Anzahl der aktivierten Module
     */
    @Override
    public int getEnabledModuleCount() {
        int count = 0;
        for (boolean enabled : moduleStatus.values()) {
            if (enabled) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Gibt die Gesamtanzahl der registrierten Module zurück
     * 
     * @return Die Gesamtanzahl der Module
     */
    @Override
    public int getTotalModuleCount() {
        return moduleStatus.size();
    }
    
    /**
     * Gibt ein ModuleStatus-Objekt für ein bestimmtes Modul zurück
     * 
     * @param moduleName Der Name des Moduls
     * @return Das ModuleStatus-Objekt oder null, wenn nicht gefunden
     */
    @Override
    public ModuleStatus getModuleStatusForModule(String moduleName) {
        return moduleStatusObjects.get(moduleName);
    }
}
