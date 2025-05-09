package de.pexora.core.logger;

import de.pexora.core.PexoraCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

/**
 * Bietet einheitliche Protokollierungsfunktionalität mit passender Formatierung.
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
     * Protokolliert eine Informationsmeldung
     * 
     * @param message Die zu protokollierende Nachricht
     */
    public void info(String message) {
        console.sendMessage(formatMessage(message, LogLevel.INFO));
    }
    
    /**
     * Protokolliert eine Warnmeldung
     * 
     * @param message Die zu protokollierende Nachricht
     */
    public void warn(String message) {
        console.sendMessage(formatMessage(message, LogLevel.WARN));
    }
    
    /**
     * Protokolliert eine Fehlermeldung
     * 
     * @param message Die zu protokollierende Nachricht
     */
    public void error(String message) {
        console.sendMessage(formatMessage(message, LogLevel.ERROR));
    }
    
    /**
     * Protokolliert eine Debug-Nachricht, wenn der Debug-Modus aktiviert ist
     * 
     * @param message Die zu protokollierende Nachricht
     */
    public void debug(String message) {
        if (plugin.getCoreConfig() != null && plugin.getCoreConfig().isDebugMode()) {
            console.sendMessage(formatMessage("[DEBUG] " + message, LogLevel.INFO));
        }
    }
    
    /**
     * Formatiert eine Nachricht mit dem entsprechenden Präfix und der passenden Farbe
     * 
     * @param message Die zu formatierende Nachricht
     * @param level Das Log-Level
     * @return Die formatierte Nachricht
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
     * Log-Level für verschiedene Nachrichtentypen
     */
    public enum LogLevel {
        INFO,
        WARN,
        ERROR
    }
}
