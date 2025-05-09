package de.pexora.core.module;

import de.pexora.core.PexoraCore;
import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Verwaltet das Laden, Aktivieren, Deaktivieren und Neuladen von Modulen.
 * 
 * Der ModuleLoader ist ein zentrales Element des PexoraCore-Systems und übernimmt 
 * das automatische Erkennen, Laden und Verwalten aller Module.
 * 
 * Funktionen:
 * - Automatisches Laden aller JAR-Dateien aus dem Modules-Verzeichnis
 * - Prüfung der Plugin-Abhängigkeiten für kompatible Module
 * - Statusverfolgung aller geladenen Module
 * - Möglichkeit zum Neuladen einzelner oder aller Module
 * - Verhinderung von Mehrfachladung identischer Module
 * 
 * Module werden als reguläre JavaPlugins behandelt, die PexoraCore als Abhängigkeit definieren.
 * Dadurch können sie alle Bukkit-API-Funktionen nutzen und gleichzeitig auf die
 * erweiterten Funktionen von PexoraCore zugreifen.
 */
public class ModuleLoader {

    private final PexoraCore core;
    private final PluginManager pluginManager;
    private final File modulesDir;
    private final Map<String, JavaPlugin> loadedModules;
    
    public ModuleLoader(PexoraCore core) {
        this.core = core;
        this.pluginManager = Bukkit.getPluginManager();
        this.modulesDir = new File(core.getDataFolder(), "modules");
        this.loadedModules = new HashMap<>();
        
        // Load all modules during initialization
        loadModules();
    }
    
    /**
     * Loads all modules from the modules directory
     */
    public void loadModules() {
        core.getLoggerService().info("Module werden geladen...");
        
        if (!modulesDir.exists()) {
            if (!modulesDir.mkdirs()) {
                core.getLoggerService().error("Fehler beim Erstellen des Modules-Verzeichnisses!");
                return;
            }
        }
        
        // Automatisches Kopieren von Plugins in das Module-Verzeichnis
        if (core.getCoreConfig().isAutoCopyToModules()) {
            copyPluginsToModules();
        }
        
        File[] files = modulesDir.listFiles((dir, name) -> name.endsWith(".jar"));
        
        if (files == null || files.length == 0) {
            core.getLoggerService().info("Keine Module zum Laden gefunden.");
            return;
        }
        
        for (File file : files) {
            try {
                loadModule(file);
            } catch (Exception e) {
                core.getLoggerService().error("Fehler beim Laden des Moduls " + file.getName() + ": " + e.getMessage());
                if (core.getCoreConfig().isDebugMode()) {
                    e.printStackTrace();
                }
            }
        }
        
        core.getLoggerService().info(loadedModules.size() + " Module erfolgreich geladen.");
    }
    
    /**
     * Kopiert Plugins aus dem Plugins-Verzeichnis in das Modules-Verzeichnis
     * und löscht optional die alten Versionen aus dem Plugins-Verzeichnis
     */
    private void copyPluginsToModules() {
        core.getLoggerService().info("Prüfe auf Plugins, die als Module geladen werden sollen...");
        
        // Plugins-Verzeichnis (ein Verzeichnis höher als unser Datenverzeichnis)
        File pluginsDir = core.getDataFolder().getParentFile();
        
        // Liste mit zusätzlichen Modulnamen aus der Konfiguration
        List<String> additionalModules = core.getCoreConfig().getAdditionalModules();
        
        // Feststellen, ob die Namenskonvention ignoriert werden soll
        boolean ignoreNamingConvention = core.getCoreConfig().isIgnoreNamingConvention();
        
        // Sollen Nicht-Pexora-Module erlaubt sein?
        boolean allowNonPexoraModules = core.getCoreConfig().isAllowNonPexoraModules();
        
        // Sollen alte Plugins gelöscht werden?
        boolean deleteOldPlugins = core.getCoreConfig().isDeleteOldPlugins();
        
        // Alle Plugins auflisten
        File[] pluginFiles = pluginsDir.listFiles((dir, name) -> name.endsWith(".jar") && !name.equals("PexoraCore.jar"));
        
        if (pluginFiles == null || pluginFiles.length == 0) {
            core.getLoggerService().debug("Keine Plugins zum Kopieren gefunden.");
            return;
        }
        
        for (File pluginFile : pluginFiles) {
            try {
                // Plugin laden, ohne es zu aktivieren
                Plugin plugin = pluginManager.loadPlugin(pluginFile);
                
                // Plugin deaktivieren, da wir es nur zum Prüfen geladen haben
                if (plugin != null) {
                    pluginManager.disablePlugin(plugin);
                    
                    String pluginName = plugin.getName();
                    
                    boolean shouldCopy = false;
                    
                    // Ist es ein Pexora-Plugin (mit dem Namensprefix) oder die Namenskonvention wird ignoriert?
                    if (pluginName.startsWith("Pexora") || ignoreNamingConvention) {
                        shouldCopy = true;
                    }
                    
                    // Wenn Nicht-Pexora-Module erlaubt sind und es in der zusätzlichen Liste steht
                    if (allowNonPexoraModules && additionalModules.contains(pluginName)) {
                        shouldCopy = true;
                    }
                    
                    // Hängt es von PexoraCore ab?
                    List<String> dependencies = plugin.getDescription().getDepend();
                    if (dependencies != null && dependencies.contains("PexoraCore")) {
                        shouldCopy = true;
                    }
                    
                    // Wenn es ein Modul ist, kopieren
                    if (shouldCopy) {
                        // Zieldatei im Modules-Verzeichnis
                        File targetFile = new File(modulesDir, pluginFile.getName());
                        
                        // Wenn die Datei bereits existiert, überspringen
                        if (targetFile.exists()) {
                            core.getLoggerService().debug("Modul " + pluginName + " existiert bereits im Modules-Verzeichnis.");
                            continue;
                        }
                        
                        // Kopieren
                        try {
                            java.nio.file.Files.copy(pluginFile.toPath(), targetFile.toPath());
                            core.getLoggerService().info("Plugin " + pluginName + " wurde als Modul kopiert.");
                            
                            // Altes Plugin löschen, wenn gewünscht
                            if (deleteOldPlugins) {
                                if (pluginFile.delete()) {
                                    core.getLoggerService().info("Altes Plugin " + pluginName + " wurde gelöscht.");
                                } else {
                                    core.getLoggerService().warn("Konnte altes Plugin " + pluginName + " nicht löschen.");
                                }
                            }
                        } catch (IOException e) {
                            core.getLoggerService().error("Fehler beim Kopieren von " + pluginName + ": " + e.getMessage());
                        }
                    }
                }
            } catch (Exception e) {
                core.getLoggerService().error("Fehler beim Überprüfen von " + pluginFile.getName() + ": " + e.getMessage());
                if (core.getCoreConfig().isDebugMode()) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Loads a single module from a file
     * 
     * @param file The module file to load
     * @throws InvalidPluginException If the plugin is invalid
     * @throws InvalidDescriptionException If the plugin description is invalid
     */
    public void loadModule(File file) throws InvalidPluginException, InvalidDescriptionException {
        if (core.getCoreConfig().isDebugMode()) {
            core.getLoggerService().info("Attempting to load module: " + file.getName());
        }
        
        Plugin plugin = pluginManager.loadPlugin(file);
        
        if (plugin == null) {
            throw new InvalidPluginException("Failed to load plugin from file " + file.getName());
        }
        
        // Verify it's a JavaPlugin
        if (!(plugin instanceof JavaPlugin)) {
            pluginManager.disablePlugin(plugin);
            throw new InvalidPluginException("Plugin " + plugin.getName() + " is not a JavaPlugin!");
        }
        
        JavaPlugin javaPlugin = (JavaPlugin) plugin;
        
        // Check if plugin depends on PexoraCore
        if (!hasPexoraDependency(javaPlugin)) {
            core.getLoggerService().warn("Module " + javaPlugin.getName() + " doesn't declare PexoraCore as a dependency. This may cause issues.");
        }
        
        // Check for Pexora prefix in name
        if (!javaPlugin.getName().startsWith("Pexora")) {
            core.getLoggerService().warn("Module " + javaPlugin.getName() + " doesn't follow the naming convention (Pexora*).");
        }
        
        // Enable the plugin
        try {
            pluginManager.enablePlugin(javaPlugin);
            loadedModules.put(javaPlugin.getName(), javaPlugin);
            core.getStatusAPI().registerModule(javaPlugin.getName(), true);
            core.getLoggerService().info("Successfully loaded and enabled module: " + javaPlugin.getName() + " v" + javaPlugin.getDescription().getVersion());
        } catch (Exception e) {
            core.getStatusAPI().registerModule(javaPlugin.getName(), false);
            core.getLoggerService().error("Failed to enable module " + javaPlugin.getName() + ": " + e.getMessage());
            if (core.getCoreConfig().isDebugMode()) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Disables all loaded modules
     */
    public void disableAllModules() {
        core.getLoggerService().info("Disabling all modules...");
        
        List<String> modules = new ArrayList<>(loadedModules.keySet());
        
        for (String moduleName : modules) {
            JavaPlugin module = loadedModules.get(moduleName);
            try {
                pluginManager.disablePlugin(module);
                core.getStatusAPI().registerModule(moduleName, false);
                core.getLoggerService().info("Disabled module: " + moduleName);
            } catch (Exception e) {
                core.getLoggerService().error("Error disabling module " + moduleName + ": " + e.getMessage());
            }
        }
        
        loadedModules.clear();
    }
    
    /**
     * Reloads all modules
     */
    public void reloadAllModules() {
        core.getLoggerService().info("Reloading all modules...");
        
        // Disable all modules
        disableAllModules();
        
        // Load all modules
        loadModules();
    }
    
    /**
     * @return A copy of the loaded modules map
     */
    public Map<String, JavaPlugin> getModules() {
        return new HashMap<>(loadedModules);
    }
    
    /**
     * Checks if a plugin depends on PexoraCore
     * 
     * @param plugin The plugin to check
     * @return True if the plugin depends on PexoraCore
     */
    private boolean hasPexoraDependency(JavaPlugin plugin) {
        List<String> dependencies = plugin.getDescription().getDepend();
        List<String> softDependencies = plugin.getDescription().getSoftDepend();
        
        return (dependencies != null && dependencies.contains("PexoraCore")) || 
               (softDependencies != null && softDependencies.contains("PexoraCore"));
    }
    
    /**
     * Gets a loaded module by name
     * 
     * @param name The name of the module
     * @return The module, or null if not found
     */
    public JavaPlugin getModule(String name) {
        return loadedModules.get(name);
    }
    
    /**
     * Gets all loaded modules
     * 
     * @return A map of module names to module instances
     */
    public Map<String, JavaPlugin> getLoadedModules() {
        return new HashMap<>(loadedModules);
    }
}
