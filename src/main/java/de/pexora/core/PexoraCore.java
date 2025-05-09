package de.pexora.core;

import de.pexora.core.api.PexoraAPI;
import de.pexora.core.commands.PexoraCommand;
import de.pexora.core.config.CoreConfig;
import de.pexora.core.config.MessageConfig;
import de.pexora.core.logger.LoggerService;
import de.pexora.core.messaging.PexoraChannel;
import de.pexora.core.module.ModuleLoader;
import de.pexora.core.status.StatusAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Hauptklasse für das PexoraCore-Plugin-System.
 * Dieses Plugin dient als Grundlage für ein modulares Plugin-System mit folgenden Funktionalitäten:
 * - Automatisches Laden von Modulen
 * - Zentrale Konfigurationsverwaltung
 * - Einheitliches Nachrichtensystem mit Adventure-API
 * - Server-übergreifende Kommunikation
 * - Statusüberwachung aller Module
 * 
 * Die Spigot-Version arbeitet nahtlos mit der Velocity-Version (PexoraProxy) zusammen, 
 * um eine durchgängige Erfahrung über den gesamten Server-Verbund zu bieten.
 */
public class PexoraCore extends JavaPlugin {

    private static PexoraCore instance;
    
    private LoggerService loggerService;
    private CoreConfig coreConfig;
    private MessageConfig messageConfig;
    private ModuleLoader moduleLoader;
    private StatusAPI statusAPI;
    private PexoraChannel pexoraChannel;
    private boolean placeholderApiAvailable = false;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Erstelle Hauptverzeichnisse
        createDirectories();
        
        // Initialisiere Komponenten
        this.loggerService = new LoggerService(this);
        
        // Zeige PEX-Logo in der Konsole
        this.loggerService.printLogo();
        
        this.loggerService.info("PexoraCore wird initialisiert...");
        
        // Lade Konfigurationen
        this.coreConfig = new CoreConfig(this);
        this.messageConfig = new MessageConfig(this);
        
        // Prüfe auf PlaceholderAPI
        checkPlaceholderAPI();
        
        // Initialisiere Status-API
        this.statusAPI = new StatusAPI();
        
        // Initialisiere Plugin-Messaging-Kanal
        this.pexoraChannel = new PexoraChannel(this);
        
        // Initialisiere Modul-Loader
        this.moduleLoader = new ModuleLoader(this);
        this.loggerService.info("Module wurden geladen: " + this.moduleLoader.getModules().size());
        
        // Initialisiere API
        PexoraAPI.initialize(this);
        
        // Registriere Befehle
        getCommand("pexoracore").setExecutor(new PexoraCommand(this));
        
        this.loggerService.info("PexoraCore wurde erfolgreich aktiviert!");
    }
    
    @Override
    public void onDisable() {
        this.loggerService.info("PexoraCore wird deaktiviert...");
        
        // Entlade alle Module
        if (this.moduleLoader != null) {
            this.moduleLoader.disableAllModules();
        }
        
        // Deregistriere den Kommunikationskanal
        if (this.pexoraChannel != null) {
            this.pexoraChannel.unregister();
        }
        
        this.loggerService.info("PexoraCore wurde erfolgreich deaktiviert!");
        instance = null;
    }
    
    private void createDirectories() {
        // Erstelle Hauptverzeichnis
        if (!getDataFolder().exists() && !getDataFolder().mkdirs()) {
            getLogger().warning("Fehler beim Erstellen des Plugin-Verzeichnisses!");
        }
        
        // Erstelle Module-Verzeichnis
        File modulesDir = new File(getDataFolder(), "modules");
        if (!modulesDir.exists() && !modulesDir.mkdirs()) {
            getLogger().warning("Fehler beim Erstellen des Module-Verzeichnisses!");
        }
    }
    
    private void checkPlaceholderAPI() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            this.placeholderApiAvailable = true;
            this.loggerService.info("PlaceholderAPI erkannt und erfolgreich eingebunden!");
        } else {
            this.placeholderApiAvailable = false;
            this.loggerService.warn("PlaceholderAPI nicht gefunden. Einige Funktionen werden möglicherweise nicht wie erwartet funktionieren.");
        }
    }
    
    /**
     * Lädt alle Konfigurationen und Module neu
     */
    public void reload() {
        this.loggerService.info("PexoraCore wird neu geladen...");
        
        // Konfigurationen neu laden
        this.coreConfig.reload();
        this.messageConfig.reload();
        
        // Module neu laden, wenn Auto-Reload aktiviert ist
        if (this.coreConfig.isModuleAutoReload()) {
            this.moduleLoader.reloadAllModules();
        }
        
        this.loggerService.info("PexoraCore wurde erfolgreich neu geladen!");
    }

    /**
     * @return die Instanz des Plugins
     */
    public static PexoraCore getInstance() {
        return instance;
    }
    
    /**
     * @return den Logger-Service
     */
    public LoggerService getLoggerService() {
        return loggerService;
    }
    
    /**
     * @return die Kern-Konfiguration
     */
    public CoreConfig getCoreConfig() {
        return coreConfig;
    }
    
    /**
     * @return die Nachrichten-Konfiguration
     */
    public MessageConfig getMessageConfig() {
        return messageConfig;
    }
    
    /**
     * @return den Modul-Loader
     */
    public ModuleLoader getModuleLoader() {
        return moduleLoader;
    }
    
    /**
     * @return die Status-API
     */
    public StatusAPI getStatusAPI() {
        return statusAPI;
    }
    
    /**
     * @return den Pexora-Kommunikationskanal
     */
    public PexoraChannel getPexoraChannel() {
        return pexoraChannel;
    }
    
    /**
     * @return ob PlaceholderAPI verfügbar ist
     */
    public boolean isPlaceholderApiAvailable() {
        return placeholderApiAvailable;
    }
}
