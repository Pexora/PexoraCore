package de.pexora.core.api.status;

/**
 * Repräsentiert den Status eines Moduls im PexoraCore-System.
 * Diese Klasse dient als Schnittstelle für Module, um ihren Status abzufragen oder zu aktualisieren.
 */
public class ModuleStatus {
    
    private final String moduleName;
    private boolean enabled;
    
    /**
     * Erstellt eine neue ModuleStatus-Instanz
     * 
     * @param moduleName Der Name des Moduls
     * @param enabled Der initiale Aktivierungsstatus
     */
    public ModuleStatus(String moduleName, boolean enabled) {
        this.moduleName = moduleName;
        this.enabled = enabled;
    }
    
    /**
     * Gibt den Namen des Moduls zurück
     * 
     * @return Der Modulname
     */
    public String getModuleName() {
        return moduleName;
    }
    
    /**
     * Prüft, ob das Modul aktiviert ist
     * 
     * @return true, wenn das Modul aktiviert ist, sonst false
     */
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * Setzt den Aktivierungsstatus des Moduls
     * 
     * @param enabled Der neue Aktivierungsstatus
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}