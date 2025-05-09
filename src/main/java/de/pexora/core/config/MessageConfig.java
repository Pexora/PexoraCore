package de.pexora.core.config;

import de.pexora.core.PexoraCore;
import de.pexora.core.util.AdventureUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages message configuration (messages.yml) for customizable messages.
 * Uses MiniMessage format for styling.
 */
public class MessageConfig {

    private final PexoraCore plugin;
    private final File messagesFile;
    private FileConfiguration config;
    
    // Cache for message components
    private final Map<String, Component> messageCache;
    private Component prefixComponent;
    
    public MessageConfig(PexoraCore plugin) {
        this.plugin = plugin;
        this.messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        this.messageCache = new HashMap<>();
        
        // Load or create messages
        if (!messagesFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        
        reload();
    }
    
    /**
     * Reloads the message configuration from disk
     */
    public void reload() {
        config = YamlConfiguration.loadConfiguration(messagesFile);
        messageCache.clear();
        
        // Cache the prefix component
        String prefixString = config.getString("prefix", "<gradient:#ff55ff:#aa00ff>[PX]</gradient> ");
        prefixComponent = MiniMessage.miniMessage().deserialize(prefixString);
        
        if (plugin.getCoreConfig().isDebugMode()) {
            plugin.getLogger().info("[DEBUG] Loaded MessageConfig, prefix: " + prefixString);
        }
    }
    
    /**
     * Saves the message configuration to disk
     */
    public void save() {
        try {
            config.save(messagesFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save messages to " + messagesFile);
            e.printStackTrace();
        }
    }
    
    /**
     * Gets a message from the configuration
     * 
     * @param key The message key
     * @return The message component, or a default message if not found
     */
    public Component getMessage(String key) {
        // Return from cache if available
        if (messageCache.containsKey(key)) {
            return messageCache.get(key);
        }
        
        // Get from config, apply prefix
        String message = config.getString(key);
        if (message == null) {
            Component missingComponent = Component.text("Missing message: " + key);
            messageCache.put(key, missingComponent);
            return missingComponent;
        }
        
        // Replace %prefix% placeholder
        message = message.replace("%prefix%", config.getString("prefix", "<gradient:#ff55ff:#aa00ff>[PX]</gradient> "));
        
        // Parse with MiniMessage
        Component component = MiniMessage.miniMessage().deserialize(message);
        messageCache.put(key, component);
        
        return component;
    }
    
    /**
     * Gets a message and replaces custom placeholders
     * 
     * @param key The message key
     * @param placeholders The placeholders to replace (key, value pairs)
     * @return The formatted message component
     */
    public Component getMessage(String key, String... placeholders) {
        String message = config.getString(key);
        if (message == null) {
            return Component.text("Missing message: " + key);
        }
        
        // Replace %prefix% placeholder
        message = message.replace("%prefix%", config.getString("prefix", "<gradient:#ff55ff:#aa00ff>[PX]</gradient> "));
        
        // Replace custom placeholders
        if (placeholders != null && placeholders.length >= 2) {
            for (int i = 0; i < placeholders.length - 1; i += 2) {
                String placeholder = placeholders[i];
                String value = placeholders[i + 1];
                message = message.replace("%" + placeholder + "%", value);
            }
        }
        
        // Parse with MiniMessage
        return MiniMessage.miniMessage().deserialize(message);
    }
    
    /**
     * Gets a message as a plain string (no component)
     * 
     * @param key The message key
     * @return The plain message string, or null if not found
     */
    public String getPlainMessage(String key) {
        return config.getString(key);
    }
    
    /**
     * Sets a message in the configuration
     * 
     * @param key The message key
     * @param message The message text
     */
    public void setMessage(String key, String message) {
        config.set(key, message);
        messageCache.remove(key);
        save();
    }
    
    /**
     * @return the prefix component
     */
    public Component getPrefixComponent() {
        return prefixComponent;
    }
    
    /**
     * @return the raw prefix string
     */
    public String getPrefixString() {
        return config.getString("prefix", "<gradient:#ff55ff:#aa00ff>[PX]</gradient> ");
    }
    
    /**
     * Sets the prefix
     * 
     * @param prefix The new prefix string
     */
    public void setPrefix(String prefix) {
        config.set("prefix", prefix);
        prefixComponent = MiniMessage.miniMessage().deserialize(prefix);
        // Clear cache as all messages with prefix need to be regenerated
        messageCache.clear();
        save();
    }
    
    /**
     * @return the configuration file
     */
    public FileConfiguration getConfig() {
        return config;
    }
}
