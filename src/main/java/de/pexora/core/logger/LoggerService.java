package de.pexora.core.logger;

import de.pexora.core.PexoraCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

/**
 * Provides consistent logging functionality with proper formatting.
 */
public class LoggerService {

    private final PexoraCore plugin;
    private final ConsoleCommandSender console;
    
    public LoggerService(PexoraCore plugin) {
        this.plugin = plugin;
        this.console = Bukkit.getConsoleSender();
    }
    
    /**
     * Zeigt das PEX-Logo in der Konsole an
     */
    public void printLogo() {
        console.sendMessage("");
        console.sendMessage(ChatColor.LIGHT_PURPLE + "  _____   ________   __");
        console.sendMessage(ChatColor.LIGHT_PURPLE + " |  __ \\ |  ____\\ \\ / /");
        console.sendMessage(ChatColor.LIGHT_PURPLE + " | |__) || |__   \\ V / ");
        console.sendMessage(ChatColor.LIGHT_PURPLE + " |  ___/ |  __|   > <  ");
        console.sendMessage(ChatColor.LIGHT_PURPLE + " | |     | |____ / . \\ ");
        console.sendMessage(ChatColor.LIGHT_PURPLE + " |_|     |______/_/ \\_\\");
        console.sendMessage("");
        console.sendMessage(ChatColor.LIGHT_PURPLE + " PexoraCore v" + plugin.getDescription().getVersion());
        console.sendMessage(ChatColor.LIGHT_PURPLE + " Entwickelt vom Pexora Development Team");
        console.sendMessage(ChatColor.LIGHT_PURPLE + " --------------------------------------");
        console.sendMessage("");
    }
    
    /**
     * Logs an informational message
     * 
     * @param message The message to log
     */
    public void info(String message) {
        console.sendMessage(formatMessage(message, LogLevel.INFO));
    }
    
    /**
     * Logs a warning message
     * 
     * @param message The message to log
     */
    public void warn(String message) {
        console.sendMessage(formatMessage(message, LogLevel.WARN));
    }
    
    /**
     * Logs an error message
     * 
     * @param message The message to log
     */
    public void error(String message) {
        console.sendMessage(formatMessage(message, LogLevel.ERROR));
    }
    
    /**
     * Logs a debug message if debug mode is enabled
     * 
     * @param message The message to log
     */
    public void debug(String message) {
        if (plugin.getCoreConfig() != null && plugin.getCoreConfig().isDebugMode()) {
            console.sendMessage(formatMessage("[DEBUG] " + message, LogLevel.INFO));
        }
    }
    
    /**
     * Formats a message with the appropriate prefix and color
     * 
     * @param message The message to format
     * @param level The log level
     * @return The formatted message
     */
    private String formatMessage(String message, LogLevel level) {
        String prefix;
        String color;
        
        switch (level) {
            case INFO:
                prefix = "[INFO]";
                color = ChatColor.GREEN.toString();
                break;
            case WARN:
                prefix = "[WARN]";
                color = ChatColor.YELLOW.toString();
                break;
            case ERROR:
                prefix = "[ERROR]";
                color = ChatColor.RED.toString();
                break;
            default:
                prefix = "[INFO]";
                color = ChatColor.GREEN.toString();
        }
        
        return ChatColor.DARK_PURPLE + "[PEX] " + color + prefix + " " + ChatColor.RESET + message;
    }
    
    /**
     * Log levels for different message types
     */
    public enum LogLevel {
        INFO,
        WARN,
        ERROR
    }
}
