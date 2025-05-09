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
        
        // Create core directories
        createDirectories();
        
        // Initialize components
        this.loggerService = new LoggerService(this);
        
        // Zeige PEX-Logo in der Konsole
        this.loggerService.printLogo();
        
        this.loggerService.info("PexoraCore wird initialisiert...");
        
        // Load configurations
        this.coreConfig = new CoreConfig(this);
        this.messageConfig = new MessageConfig(this);
        
        // Check for PlaceholderAPI
        checkPlaceholderAPI();
        
        // Initialize status API
        this.statusAPI = new StatusAPI();
        
        // Initialize plugin messaging channel
        this.pexoraChannel = new PexoraChannel(this);
        
        // Initialize module loader
        this.moduleLoader = new ModuleLoader(this);
        this.loggerService.info("Module wurden geladen: " + this.moduleLoader.getModules().size());
        
        // Initialize API
        PexoraAPI.initialize(this);
        
        // Register commands
        getCommand("pexora").setExecutor(new PexoraCommand(this));
        
        this.loggerService.info("PexoraCore wurde erfolgreich aktiviert!");
    }
    
    @Override
    public void onDisable() {
        this.loggerService.info("Disabling PexoraCore...");
        
        // Unload modules
        if (this.moduleLoader != null) {
            this.moduleLoader.disableAllModules();
        }
        
        // Unregister channel
        if (this.pexoraChannel != null) {
            this.pexoraChannel.unregister();
        }
        
        this.loggerService.info("PexoraCore has been disabled!");
        instance = null;
    }
    
    private void createDirectories() {
        // Create main directory
        if (!getDataFolder().exists() && !getDataFolder().mkdirs()) {
            getLogger().warning("Failed to create plugin directory!");
        }
        
        // Create modules directory
        File modulesDir = new File(getDataFolder(), "modules");
        if (!modulesDir.exists() && !modulesDir.mkdirs()) {
            getLogger().warning("Failed to create modules directory!");
        }
    }
    
    private void checkPlaceholderAPI() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            this.placeholderApiAvailable = true;
            this.loggerService.info("PlaceholderAPI detected and hooked successfully!");
        } else {
            this.placeholderApiAvailable = false;
            this.loggerService.warn("PlaceholderAPI not detected. Some features may not work as expected.");
        }
    }
    
    /**
     * Reloads all configurations and modules
     */
    public void reload() {
        this.loggerService.info("Reloading PexoraCore...");
        
        // Reload configs
        this.coreConfig.reload();
        this.messageConfig.reload();
        
        // Reload modules if auto-reload is enabled
        if (this.coreConfig.isModuleAutoReload()) {
            this.moduleLoader.reloadAllModules();
        }
        
        this.loggerService.info("PexoraCore has been reloaded!");
    }

    /**
     * @return the instance of the plugin
     */
    public static PexoraCore getInstance() {
        return instance;
    }
    
    /**
     * @return the logger service
     */
    public LoggerService getLoggerService() {
        return loggerService;
    }
    
    /**
     * @return the core configuration
     */
    public CoreConfig getCoreConfig() {
        return coreConfig;
    }
    
    /**
     * @return the message configuration
     */
    public MessageConfig getMessageConfig() {
        return messageConfig;
    }
    
    /**
     * @return the module loader
     */
    public ModuleLoader getModuleLoader() {
        return moduleLoader;
    }
    
    /**
     * @return the status API
     */
    public StatusAPI getStatusAPI() {
        return statusAPI;
    }
    
    /**
     * @return the Pexora channel
     */
    public PexoraChannel getPexoraChannel() {
        return pexoraChannel;
    }
    
    /**
     * @return whether PlaceholderAPI is available
     */
    public boolean isPlaceholderApiAvailable() {
        return placeholderApiAvailable;
    }
}
