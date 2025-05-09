package de.pexora.core.api;

import de.pexora.core.PexoraCore;
import de.pexora.core.config.CoreConfig;
import de.pexora.core.config.MessageConfig;
import de.pexora.core.logger.LoggerService;
import de.pexora.core.module.ModuleLoader;
import de.pexora.core.status.StatusAPI;
import net.kyori.adventure.text.Component;

import java.util.Map;

/**
 * API-Schnittstelle für die Interaktion anderer Plugins mit PexoraCore.
 * 
 * Die PexoraAPI ist die zentrale Schnittstelle für alle Module und externen Plugins,
 * um auf die Funktionen des PexoraCore-Systems zuzugreifen. Sie folgt dem Singleton-Muster
 * und kann über PexoraAPI.get() von überall erreicht werden.
 * 
 * Über diese API können Module auf folgende Funktionen zugreifen:
 * - Logger-Services für einheitliche Konsolenausgaben
 * - Konfigurationsverwaltung (Core und Nachrichten)
 * - Prüfung auf PlaceholderAPI-Unterstützung
 * - Zugriff auf geladene Module und ihren Status
 * - Nachrichten mit Adventure-Komponenten
 * 
 * Das Design ermöglicht eine saubere Trennung zwischen dem Core und den Modulen,
 * während alle wichtigen Dienste über diese zentrale Schnittstelle bereitgestellt werden.
 */
public class PexoraAPI {

    private static PexoraAPI instance;
    private final PexoraCore plugin;
    
    /**
     * Initialisiert die API mit der Core-Plugin-Instanz.
     * Sollte nur von PexoraCore selbst aufgerufen werden.
     * 
     * @param plugin Die PexoraCore-Plugin-Instanz
     */
    public static void initialize(PexoraCore plugin) {
        if (instance == null) {
            instance = new PexoraAPI(plugin);
        }
    }
    
    /**
     * Gibt die API-Instanz zurück.
     * 
     * @return Die PexoraAPI-Instanz
     */
    public static PexoraAPI get() {
        if (instance == null) {
            throw new IllegalStateException("PexoraAPI wurde noch nicht initialisiert!");
        }
        return instance;
    }
    
    private PexoraAPI(PexoraCore plugin) {
        this.plugin = plugin;
    }
    
    /**
     * @return Die Core-Plugin-Instanz
     */
    public PexoraCore getPlugin() {
        return plugin;
    }
    
    /**
     * @return Die Kern-Konfiguration
     */
    public CoreConfig getCoreConfig() {
        return plugin.getCoreConfig();
    }
    
    /**
     * @return Die Nachrichten-Konfiguration
     */
    public MessageConfig getMessageConfig() {
        return plugin.getMessageConfig();
    }
    
    /**
     * @return Den Logger-Service
     */
    public LoggerService getLoggerService() {
        return plugin.getLoggerService();
    }
    
    /**
     * @return Den Modul-Loader
     */
    public ModuleLoader getModuleLoader() {
        return plugin.getModuleLoader();
    }
    
    /**
     * @return Ob PlaceholderAPI verfügbar ist
     */
    public boolean isPlaceholderApiAvailable() {
        return plugin.isPlaceholderApiAvailable();
    }
    
    /**
     * @return Die Status-API mit Modulzuständen
     */
    public StatusAPI getStatusAPI() {
        return plugin.getStatusAPI();
    }
    
    /**
     * Gibt den Status aller Module zurück
     * 
     * @return Eine Map mit Modulnamen und ihrem Aktivierungsstatus
     */
    public Map<String, Boolean> getModuleStatus() {
        return plugin.getStatusAPI().getModuleStatus();
    }
    
    /**
     * Gibt die Prefix-Komponente für Nachrichten zurück
     * 
     * @return Die Prefix-Komponente
     */
    public Component getPrefixComponent() {
        return plugin.getMessageConfig().getPrefixComponent();
    }
    
    /**
     * Holt eine Nachricht aus der Nachrichtenkonfiguration und ersetzt Platzhalter
     * 
     * @param key Der Nachrichtenschlüssel
     * @return Die formatierte Nachrichtenkomponente
     */
    public Component getMessage(String key) {
        return plugin.getMessageConfig().getMessage(key);
    }
    
    /**
     * Holt eine Nachricht und ersetzt benutzerdefinierte Platzhalter
     * 
     * @param key Der Nachrichtenschlüssel
     * @param placeholders Die zu ersetzenden Platzhalter (Schlüssel-Wert-Paare)
     * @return Die formatierte Nachrichtenkomponente
     */
    public Component getMessage(String key, String... placeholders) {
        return plugin.getMessageConfig().getMessage(key, placeholders);
    }
    
    /**
     * Protokolliert eine Info-Nachricht über den Logger-Service
     * 
     * @param message Die zu protokollierende Nachricht
     */
    public void info(String message) {
        plugin.getLoggerService().info(message);
    }
    
    /**
     * Protokolliert eine Warnmeldung über den Logger-Service
     * 
     * @param message Die zu protokollierende Nachricht
     */
    public void warn(String message) {
        plugin.getLoggerService().warn(message);
    }
    
    /**
     * Protokolliert eine Fehlermeldung über den Logger-Service
     * 
     * @param message Die zu protokollierende Nachricht
     */
    public void error(String message) {
        plugin.getLoggerService().error(message);
    }
    
    /**
     * Protokolliert eine Debug-Nachricht, wenn der Debug-Modus aktiviert ist
     * 
     * @param message Die zu protokollierende Nachricht
     */
    public void debug(String message) {
        if (plugin.getCoreConfig().isDebugMode()) {
            plugin.getLoggerService().info("[DEBUG] " + message);
        }
    }
}
