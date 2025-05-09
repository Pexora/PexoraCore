package de.pexora.core.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

/**
 * Utility class for working with Adventure components and formatting.
 */
public class AdventureUtil {

    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    private static final LegacyComponentSerializer legacySerializer = LegacyComponentSerializer.builder()
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build();
    
    /**
     * Converts a MiniMessage format string to a Component
     * 
     * @param input The MiniMessage format string
     * @return The parsed Component
     */
    public static Component parse(String input) {
        return miniMessage.deserialize(input);
    }
    
    /**
     * Converts a Component to a MiniMessage format string
     * 
     * @param component The Component to serialize
     * @return The MiniMessage format string
     */
    public static String serialize(Component component) {
        return miniMessage.serialize(component);
    }
    
    /**
     * Converts a legacy formatted string (with &-codes) to a Component
     * 
     * @param input The legacy formatted string
     * @return The Component
     */
    public static Component fromLegacy(String input) {
        return legacySerializer.deserialize(input);
    }
    
    /**
     * Converts a Component to a legacy formatted string
     * 
     * @param component The Component to convert
     * @return The legacy formatted string
     */
    public static String toLegacy(Component component) {
        return legacySerializer.serialize(component);
    }
    
    /**
     * Sends a Component to a player
     * 
     * @param player The player to send to
     * @param component The Component to send
     */
    public static void sendMessage(Player player, Component component) {
        player.sendMessage(toLegacy(component));
    }
    
    /**
     * Sends a MiniMessage formatted string to a player
     * 
     * @param player The player to send to
     * @param message The MiniMessage formatted string
     */
    public static void sendMessage(Player player, String message) {
        player.sendMessage(toLegacy(parse(message)));
    }
    
    /**
     * Replaces placeholders in a MiniMessage format string
     * 
     * @param input The input string with placeholders
     * @param placeholders An array of placeholder, value pairs
     * @return The string with placeholders replaced
     */
    public static String replacePlaceholders(String input, String... placeholders) {
        if (placeholders == null || placeholders.length < 2) {
            return input;
        }
        
        String result = input;
        for (int i = 0; i < placeholders.length - 1; i += 2) {
            String placeholder = placeholders[i];
            String value = placeholders[i + 1];
            result = result.replace("%" + placeholder + "%", value);
        }
        
        return result;
    }
}
