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
     * Initialize the API with the core plugin instance.
     * Should only be called by PexoraCore itself.
     * 
     * @param plugin The PexoraCore plugin instance
     */
    public static void initialize(PexoraCore plugin) {
        if (instance == null) {
            instance = new PexoraAPI(plugin);
        }
    }
    
    /**
     * Gets the API instance.
     * 
     * @return The PexoraAPI instance
     */
    public static PexoraAPI get() {
        if (instance == null) {
            throw new IllegalStateException("PexoraAPI has not been initialized yet!");
        }
        return instance;
    }
    
    private PexoraAPI(PexoraCore plugin) {
        this.plugin = plugin;
    }
    
    /**
     * @return The core plugin instance
     */
    public PexoraCore getPlugin() {
        return plugin;
    }
    
    /**
     * @return The core configuration
     */
    public CoreConfig getCoreConfig() {
        return plugin.getCoreConfig();
    }
    
    /**
     * @return The message configuration
     */
    public MessageConfig getMessageConfig() {
        return plugin.getMessageConfig();
    }
    
    /**
     * @return The logger service
     */
    public LoggerService getLoggerService() {
        return plugin.getLoggerService();
    }
    
    /**
     * @return The module loader
     */
    public ModuleLoader getModuleLoader() {
        return plugin.getModuleLoader();
    }
    
    /**
     * @return Whether PlaceholderAPI is available
     */
    public boolean isPlaceholderApiAvailable() {
        return plugin.isPlaceholderApiAvailable();
    }
    
    /**
     * @return The status API containing module states
     */
    public StatusAPI getStatusAPI() {
        return plugin.getStatusAPI();
    }
    
    /**
     * Gets the status of all modules
     * 
     * @return A map of module names to their enabled status
     */
    public Map<String, Boolean> getModuleStatus() {
        return plugin.getStatusAPI().getModuleStatus();
    }
    
    /**
     * Gets the prefix component for messages
     * 
     * @return The prefix component
     */
    public Component getPrefixComponent() {
        return plugin.getMessageConfig().getPrefixComponent();
    }
    
    /**
     * Gets a message from the message config and replaces placeholders
     * 
     * @param key The message key
     * @return The formatted message component
     */
    public Component getMessage(String key) {
        return plugin.getMessageConfig().getMessage(key);
    }
    
    /**
     * Gets a message and replaces custom placeholders
     * 
     * @param key The message key
     * @param placeholders The placeholders to replace (key, value pairs)
     * @return The formatted message component
     */
    public Component getMessage(String key, String... placeholders) {
        return plugin.getMessageConfig().getMessage(key, placeholders);
    }
    
    /**
     * Logs an info message through the logger service
     * 
     * @param message The message to log
     */
    public void info(String message) {
        plugin.getLoggerService().info(message);
    }
    
    /**
     * Logs a warning message through the logger service
     * 
     * @param message The message to log
     */
    public void warn(String message) {
        plugin.getLoggerService().warn(message);
    }
    
    /**
     * Logs an error message through the logger service
     * 
     * @param message The message to log
     */
    public void error(String message) {
        plugin.getLoggerService().error(message);
    }
    
    /**
     * Logs a debug message if debug mode is enabled
     * 
     * @param message The message to log
     */
    public void debug(String message) {
        if (plugin.getCoreConfig().isDebugMode()) {
            plugin.getLoggerService().info("[DEBUG] " + message);
        }
    }
}
