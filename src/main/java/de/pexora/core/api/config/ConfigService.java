package de.pexora.core.api.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Service zum Verwalten von Konfigurationsdateien für Module.
 * Diese Klasse bietet Methoden zum Laden, Speichern und Bearbeiten von YAML-Konfigurationen.
 */
public class ConfigService {
    
    private final File configFile;
    private FileConfiguration config;
    
    /**
     * Erstellt einen neuen ConfigService für eine bestimmte Datei
     * 
     * @param configFile Die Konfigurationsdatei
     */
    public ConfigService(File configFile) {
        this.configFile = configFile;
        this.reload();
    }
    
    /**
     * Lädt die Konfiguration neu aus der Datei
     */
    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(configFile);
    }
    
    /**
     * Speichert die aktuelle Konfiguration in die Datei
     * 
     * @return true bei Erfolg, sonst false
     */
    public boolean save() {
        try {
            config.save(configFile);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    /**
     * Gibt die FileConfiguration zurück
     * 
     * @return Die Bukkit FileConfiguration
     */
    public FileConfiguration getConfig() {
        return config;
    }
    
    /**
     * Prüft, ob ein Pfad in der Konfiguration existiert
     * 
     * @param path Der Konfigurationspfad
     * @return true, wenn der Pfad existiert, sonst false
     */
    public boolean contains(String path) {
        return config.contains(path);
    }
    
    /**
     * Holt einen String aus der Konfiguration
     * 
     * @param path Der Konfigurationspfad
     * @param defaultValue Der Standardwert, falls nicht gefunden
     * @return Der Wert oder der Standardwert
     */
    public String getString(String path, String defaultValue) {
        return config.getString(path, defaultValue);
    }
    
    /**
     * Holt einen Integer aus der Konfiguration
     * 
     * @param path Der Konfigurationspfad
     * @param defaultValue Der Standardwert, falls nicht gefunden
     * @return Der Wert oder der Standardwert
     */
    public int getInt(String path, int defaultValue) {
        return config.getInt(path, defaultValue);
    }
    
    /**
     * Holt einen Boolean aus der Konfiguration
     * 
     * @param path Der Konfigurationspfad
     * @param defaultValue Der Standardwert, falls nicht gefunden
     * @return Der Wert oder der Standardwert
     */
    public boolean getBoolean(String path, boolean defaultValue) {
        return config.getBoolean(path, defaultValue);
    }
    
    /**
     * Holt eine Double aus der Konfiguration
     * 
     * @param path Der Konfigurationspfad
     * @param defaultValue Der Standardwert, falls nicht gefunden
     * @return Der Wert oder der Standardwert
     */
    public double getDouble(String path, double defaultValue) {
        return config.getDouble(path, defaultValue);
    }
    
    /**
     * Holt eine Liste von Strings aus der Konfiguration
     * 
     * @param path Der Konfigurationspfad
     * @return Die Liste oder eine leere Liste, falls nicht gefunden
     */
    public List<String> getStringList(String path) {
        return config.getStringList(path);
    }
    
    /**
     * Setzt einen Wert in der Konfiguration
     * 
     * @param path Der Konfigurationspfad
     * @param value Der zu setzende Wert
     */
    public void set(String path, Object value) {
        config.set(path, value);
    }
}