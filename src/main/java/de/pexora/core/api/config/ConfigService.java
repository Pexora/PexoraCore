package de.pexora.core.api.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Service zum Laden, Speichern und Bearbeiten von Konfigurationsdateien.
 */
public class ConfigService {

    private final File configFile;
    private FileConfiguration config;

    /**
     * Erstellt einen neuen ConfigService und lädt die Konfiguration
     *
     * @param configFile Die Konfigurationsdatei
     */
    public ConfigService(File configFile) {
        this.configFile = configFile;
        reload();
    }

    /**
     * Lädt die Konfiguration neu von der Festplatte
     */
    public void reload() {
        if (!configFile.exists()) {
            try {
                if (!configFile.getParentFile().exists()) {
                    configFile.getParentFile().mkdirs();
                }
                configFile.createNewFile();
            } catch (IOException e) {
                System.err.println("Fehler beim Erstellen der Konfigurationsdatei: " + e.getMessage());
            }
        }
        this.config = YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * Speichert die Konfiguration auf die Festplatte
     */
    public void save() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            System.err.println("Fehler beim Speichern der Konfigurationsdatei: " + e.getMessage());
        }
    }

    /**
     * Gibt einen String aus der Konfiguration zurück
     * 
     * @param path Der Pfad zum Wert
     * @param defaultValue Der Standardwert, falls der Pfad nicht existiert
     * @return Der Wert an dem Pfad oder der Standardwert
     */
    public String getString(String path, String defaultValue) {
        return config.getString(path, defaultValue);
    }

    /**
     * Gibt einen String aus der Konfiguration zurück
     * 
     * @param path Der Pfad zum Wert
     * @return Der Wert an dem Pfad oder null, falls nicht vorhanden
     */
    public String getString(String path) {
        return config.getString(path);
    }

    /**
     * Gibt einen int-Wert aus der Konfiguration zurück
     * 
     * @param path Der Pfad zum Wert
     * @param defaultValue Der Standardwert, falls der Pfad nicht existiert
     * @return Der Wert an dem Pfad oder der Standardwert
     */
    public int getInt(String path, int defaultValue) {
        return config.getInt(path, defaultValue);
    }

    /**
     * Gibt einen int-Wert aus der Konfiguration zurück
     * 
     * @param path Der Pfad zum Wert
     * @return Der Wert an dem Pfad oder 0, falls nicht vorhanden
     */
    public int getInt(String path) {
        return config.getInt(path);
    }

    /**
     * Gibt einen boolean-Wert aus der Konfiguration zurück
     * 
     * @param path Der Pfad zum Wert
     * @param defaultValue Der Standardwert, falls der Pfad nicht existiert
     * @return Der Wert an dem Pfad oder der Standardwert
     */
    public boolean getBoolean(String path, boolean defaultValue) {
        return config.getBoolean(path, defaultValue);
    }

    /**
     * Gibt einen boolean-Wert aus der Konfiguration zurück
     * 
     * @param path Der Pfad zum Wert
     * @return Der Wert an dem Pfad oder false, falls nicht vorhanden
     */
    public boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    /**
     * Gibt eine Liste aus der Konfiguration zurück
     * 
     * @param path Der Pfad zum Wert
     * @return Die Liste an dem Pfad oder eine leere Liste, falls nicht vorhanden
     */
    public List<String> getStringList(String path) {
        return config.getStringList(path);
    }

    /**
     * Gibt eine ConfigurationSection aus der Konfiguration zurück
     * 
     * @param path Der Pfad zum Wert
     * @return Die Section an dem Pfad oder null, falls nicht vorhanden
     */
    public ConfigurationSection getConfigurationSection(String path) {
        return config.getConfigurationSection(path);
    }

    /**
     * Prüft, ob ein Pfad in der Konfiguration existiert
     * 
     * @param path Der zu prüfende Pfad
     * @return true, wenn der Pfad existiert, sonst false
     */
    public boolean contains(String path) {
        return config.contains(path);
    }

    /**
     * Setzt einen Wert in der Konfiguration
     * 
     * @param path Der Pfad, an dem der Wert gesetzt werden soll
     * @param value Der zu setzende Wert
     */
    public void set(String path, Object value) {
        config.set(path, value);
    }

    /**
     * Gibt alle Schlüssel in einer Section zurück
     * 
     * @param path Der Pfad zur Section
     * @param deep true, um auch Unterschlüssel zurückzugeben
     * @return Ein Set mit allen Schlüsseln
     */
    public Set<String> getKeys(String path, boolean deep) {
        ConfigurationSection section = config.getConfigurationSection(path);
        if (section != null) {
            return section.getKeys(deep);
        }
        return null;
    }

    /**
     * Gibt alle Schlüssel in der Root-Section zurück
     * 
     * @param deep true, um auch Unterschlüssel zurückzugeben
     * @return Ein Set mit allen Schlüsseln
     */
    public Set<String> getKeys(boolean deep) {
        return config.getKeys(deep);
    }

    /**
     * @return die Konfigurationsdatei
     */
    public File getConfigFile() {
        return configFile;
    }

    /**
     * @return die FileConfiguration-Instanz
     */
    public FileConfiguration getConfig() {
        return config;
    }
}