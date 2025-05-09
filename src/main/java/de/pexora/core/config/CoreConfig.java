package de.pexora.core.config;

import de.pexora.core.PexoraCore;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Manages the core configuration (config.yml) for central settings.
 */
public class CoreConfig {

    private final PexoraCore plugin;
    private final File configFile;
    private FileConfiguration config;
    
    // Configuration defaults
    private boolean debugMode = false;
    private boolean moduleAutoReload = true;
    
    // Modul-Einstellungen
    private boolean allowNonPexoraModules = true;
    private boolean ignoreNamingConvention = false;
    private boolean autoCopyToModules = false;
    private boolean deleteOldPlugins = false;
    private java.util.List<String> additionalModules = new java.util.ArrayList<>();
    
    // Datenbankeinstellungen
    private String databaseHost = "localhost";
    private int databasePort = 3306;
    private String databaseName = "pexora";
    private String databaseUser = "root";
    private String databasePassword = "";
    private boolean databaseEnabled = false;
    
    public CoreConfig(PexoraCore plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "config.yml");
        
        // Load or create config
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        
        reload();
    }
    
    /**
     * Reloads the configuration from disk
     */
    public void reload() {
        config = YamlConfiguration.loadConfiguration(configFile);
        loadValues();
    }
    
    /**
     * Saves the configuration to disk
     */
    public void save() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save config to " + configFile);
            e.printStackTrace();
        }
    }
    
    /**
     * Loads values from the configuration
     */
    private void loadValues() {
        debugMode = config.getBoolean("debug-mode", false);
        moduleAutoReload = config.getBoolean("module-auto-reload", true);
        
        // Modul-Einstellungen
        allowNonPexoraModules = config.getBoolean("modules.allow-non-pexora-modules", true);
        ignoreNamingConvention = config.getBoolean("modules.ignore-naming-convention", false);
        autoCopyToModules = config.getBoolean("modules.auto-copy-to-modules", false);
        deleteOldPlugins = config.getBoolean("modules.delete-old-plugins", false);
        additionalModules = config.getStringList("modules.additional-modules");
        
        // Falls Liste nicht existiert, leere Liste erstellen
        if (additionalModules == null) {
            additionalModules = new java.util.ArrayList<>();
        }
        
        // Datenbankeinstellungen
        databaseHost = config.getString("database.host", "localhost");
        databasePort = config.getInt("database.port", 3306);
        databaseName = config.getString("database.name", "pexora");
        databaseUser = config.getString("database.user", "root");
        databasePassword = config.getString("database.password", "");
        databaseEnabled = config.getBoolean("database.enabled", false);
        
        // Log loaded settings if in debug mode
        if (debugMode) {
            plugin.getLoggerService().debug("Geladene CoreConfig:");
            plugin.getLoggerService().debug("  Debug-Modus: " + debugMode);
            plugin.getLoggerService().debug("  Modul-Auto-Reload: " + moduleAutoReload);
            plugin.getLoggerService().debug("  Nicht-Pexora-Module erlaubt: " + allowNonPexoraModules);
            plugin.getLoggerService().debug("  Namenskonvention ignorieren: " + ignoreNamingConvention);
            plugin.getLoggerService().debug("  Auto-Copy zu Modules: " + autoCopyToModules);
            plugin.getLoggerService().debug("  Alte Plugins löschen: " + deleteOldPlugins);
            plugin.getLoggerService().debug("  Zusätzliche Module: " + additionalModules);
            plugin.getLoggerService().debug("  Datenbank aktiviert: " + databaseEnabled);
        }
    }
    
    /**
     * @return whether debug mode is enabled
     */
    public boolean isDebugMode() {
        return debugMode;
    }
    
    /**
     * Sets the debug mode
     * 
     * @param debugMode the new debug mode state
     */
    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
        config.set("debug-mode", debugMode);
        save();
    }
    
    /**
     * @return whether modules should automatically reload
     */
    public boolean isModuleAutoReload() {
        return moduleAutoReload;
    }
    
    /**
     * Sets whether modules should automatically reload
     * 
     * @param moduleAutoReload the new module auto-reload state
     */
    public void setModuleAutoReload(boolean moduleAutoReload) {
        this.moduleAutoReload = moduleAutoReload;
        config.set("module-auto-reload", moduleAutoReload);
        save();
    }
    
    /**
     * @return the database host
     */
    public String getDatabaseHost() {
        return databaseHost;
    }
    
    /**
     * @return the database port
     */
    public int getDatabasePort() {
        return databasePort;
    }
    
    /**
     * @return the database name
     */
    public String getDatabaseName() {
        return databaseName;
    }
    
    /**
     * @return the database user
     */
    public String getDatabaseUser() {
        return databaseUser;
    }
    
    /**
     * @return the database password
     */
    public String getDatabasePassword() {
        return databasePassword;
    }
    
    /**
     * @return whether the database is enabled
     */
    public boolean isDatabaseEnabled() {
        return databaseEnabled;
    }
    
    /**
     * @return the configuration file
     */
    public FileConfiguration getConfig() {
        return config;
    }
    
    /**
     * @return ob Nicht-Pexora-Module erlaubt sind
     */
    public boolean isAllowNonPexoraModules() {
        return allowNonPexoraModules;
    }
    
    /**
     * @return ob die Namenskonvention ignoriert werden soll
     */
    public boolean isIgnoreNamingConvention() {
        return ignoreNamingConvention;
    }
    
    /**
     * @return ob Plugins automatisch in das Modules-Verzeichnis kopiert werden sollen
     */
    public boolean isAutoCopyToModules() {
        return autoCopyToModules;
    }
    
    /**
     * @return ob alte Plugins nach dem Kopieren gelöscht werden sollen
     */
    public boolean isDeleteOldPlugins() {
        return deleteOldPlugins;
    }
    
    /**
     * @return die Liste zusätzlicher Module
     */
    public java.util.List<String> getAdditionalModules() {
        return additionalModules;
    }
    
    /**
     * Setzt ob Nicht-Pexora-Module erlaubt sind
     * 
     * @param allowNonPexoraModules Ob Nicht-Pexora-Module erlaubt sind
     */
    public void setAllowNonPexoraModules(boolean allowNonPexoraModules) {
        this.allowNonPexoraModules = allowNonPexoraModules;
        config.set("modules.allow-non-pexora-modules", allowNonPexoraModules);
        save();
    }
    
    /**
     * Setzt ob die Namenskonvention ignoriert werden soll
     * 
     * @param ignoreNamingConvention Ob die Namenskonvention ignoriert werden soll
     */
    public void setIgnoreNamingConvention(boolean ignoreNamingConvention) {
        this.ignoreNamingConvention = ignoreNamingConvention;
        config.set("modules.ignore-naming-convention", ignoreNamingConvention);
        save();
    }
    
    /**
     * Setzt ob Plugins automatisch in das Modules-Verzeichnis kopiert werden sollen
     * 
     * @param autoCopyToModules Ob Plugins automatisch kopiert werden sollen
     */
    public void setAutoCopyToModules(boolean autoCopyToModules) {
        this.autoCopyToModules = autoCopyToModules;
        config.set("modules.auto-copy-to-modules", autoCopyToModules);
        save();
    }
    
    /**
     * Setzt ob alte Plugins nach dem Kopieren gelöscht werden sollen
     * 
     * @param deleteOldPlugins Ob alte Plugins gelöscht werden sollen
     */
    public void setDeleteOldPlugins(boolean deleteOldPlugins) {
        this.deleteOldPlugins = deleteOldPlugins;
        config.set("modules.delete-old-plugins", deleteOldPlugins);
        save();
    }
    
    /**
     * Fügt ein Plugin zur Liste der zusätzlichen Module hinzu
     * 
     * @param moduleName Der Name des Plugins
     */
    public void addAdditionalModule(String moduleName) {
        if (!additionalModules.contains(moduleName)) {
            additionalModules.add(moduleName);
            config.set("modules.additional-modules", additionalModules);
            save();
        }
    }
    
    /**
     * Entfernt ein Plugin aus der Liste der zusätzlichen Module
     * 
     * @param moduleName Der Name des Plugins
     */
    public void removeAdditionalModule(String moduleName) {
        if (additionalModules.contains(moduleName)) {
            additionalModules.remove(moduleName);
            config.set("modules.additional-modules", additionalModules);
            save();
        }
    }
}
