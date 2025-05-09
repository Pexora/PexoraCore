package de.pexora.core.commands;

import de.pexora.core.PexoraCore;
import de.pexora.core.api.PexoraAPI;
import de.pexora.core.util.AdventureUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * Verarbeitet den Haupt-PexoraCore-Befehl und seine Unterbefehle.
 */
public class PexoraCommand implements CommandExecutor {

    private final PexoraCore plugin;

    public PexoraCommand(PexoraCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Prüfen, ob der Befehl /pexoracore ist
        if (!command.getName().equalsIgnoreCase("pexoracore")) {
            return false;
        }

        // Berechtigungen prüfen
        if (!sender.hasPermission("pexora.admin")) {
            Component message = plugin.getMessageConfig().getMessage("error-command-permission");
            if (sender instanceof Player) {
                AdventureUtil.sendMessage((Player) sender, message);
            } else {
                sender.sendMessage(AdventureUtil.toLegacy(message));
            }
            return true;
        }

        // Unterbefehle verarbeiten
        if (args.length == 0) {
            // Kein Unterbefehl angegeben, Hilfe anzeigen
            showHelp(sender);
            return true;
        }

        String subCommand = args[0].toLowerCase();
        switch (subCommand) {
            case "reload":
                handleReload(sender);
                break;
            case "status":
                handleStatus(sender);
                break;
            case "help":
                showHelp(sender);
                break;
            default:
                // Unbekannter Unterbefehl
                Component message = plugin.getMessageConfig().getMessage("error-invalid-command");
                if (sender instanceof Player) {
                    AdventureUtil.sendMessage((Player) sender, message);
                } else {
                    sender.sendMessage(AdventureUtil.toLegacy(message));
                }
                break;
        }

        return true;
    }

    private void handleReload(CommandSender sender) {
        // Plugin neu laden
        plugin.reload();
        
        // Bestätigungsnachricht senden
        Component message = plugin.getMessageConfig().getMessage("plugin-reloaded");
        if (sender instanceof Player) {
            AdventureUtil.sendMessage((Player) sender, message);
        } else {
            sender.sendMessage(AdventureUtil.toLegacy(message));
        }
    }

    private void handleStatus(CommandSender sender) {
        // Plugin-Version abrufen
        String version = plugin.getDescription().getVersion();
        
        // Modulstatus abrufen
        Map<String, Boolean> moduleStatus = plugin.getStatusAPI().getModuleStatus();
        int enabledCount = plugin.getStatusAPI().getEnabledModuleCount();
        int totalCount = plugin.getStatusAPI().getTotalModuleCount();
        
        // Header senden
        Component header = plugin.getMessageConfig().getMessage("status-header");
        Component versionInfo = plugin.getMessageConfig().getMessage("status-version", "version", version);
        Component moduleCountInfo = plugin.getMessageConfig().getMessage("status-modules", "count", enabledCount + "/" + totalCount);
        
        // PlaceholderAPI-Status senden
        String papiStatus = plugin.isPlaceholderApiAvailable() ? "§aInstalliert" : "§cNicht installiert";
        Component papiInfo = plugin.getMessageConfig().getMessage("status-placeholderapi", "status", papiStatus);
        
        // Nachrichten senden
        if (sender instanceof Player) {
            Player player = (Player) sender;
            AdventureUtil.sendMessage(player, header);
            AdventureUtil.sendMessage(player, versionInfo);
            AdventureUtil.sendMessage(player, moduleCountInfo);
            AdventureUtil.sendMessage(player, papiInfo);
            
            // Modulliste senden
            for (Map.Entry<String, Boolean> entry : moduleStatus.entrySet()) {
                String moduleStatusText = entry.getValue() ? "§aAktiviert" : "§cDeaktiviert";
                Component moduleEntry = plugin.getMessageConfig().getMessage("status-module-entry", 
                        "module", entry.getKey(), 
                        "status", moduleStatusText);
                AdventureUtil.sendMessage(player, moduleEntry);
            }
        } else {
            // Konsolensender
            sender.sendMessage(AdventureUtil.toLegacy(header));
            sender.sendMessage(AdventureUtil.toLegacy(versionInfo));
            sender.sendMessage(AdventureUtil.toLegacy(moduleCountInfo));
            sender.sendMessage(AdventureUtil.toLegacy(papiInfo));
            
            // Modulliste senden
            for (Map.Entry<String, Boolean> entry : moduleStatus.entrySet()) {
                String moduleStatusText = entry.getValue() ? "Aktiviert" : "Deaktiviert";
                Component moduleEntry = plugin.getMessageConfig().getMessage("status-module-entry", 
                        "module", entry.getKey(), 
                        "status", moduleStatusText);
                sender.sendMessage(AdventureUtil.toLegacy(moduleEntry));
            }
        }
    }

    private void showHelp(CommandSender sender) {
        // Nachrichten abrufen
        Component header = plugin.getMessageConfig().getMessage("help-header");
        Component reloadHelp = plugin.getMessageConfig().getMessage("help-command-reload");
        Component statusHelp = plugin.getMessageConfig().getMessage("help-command-status");
        Component helpHelp = plugin.getMessageConfig().getMessage("help-command-help");
        
        // Nachrichten senden
        if (sender instanceof Player) {
            Player player = (Player) sender;
            AdventureUtil.sendMessage(player, header);
            AdventureUtil.sendMessage(player, reloadHelp);
            AdventureUtil.sendMessage(player, statusHelp);
            AdventureUtil.sendMessage(player, helpHelp);
        } else {
            // Konsolensender
            sender.sendMessage(AdventureUtil.toLegacy(header));
            sender.sendMessage(AdventureUtil.toLegacy(reloadHelp));
            sender.sendMessage(AdventureUtil.toLegacy(statusHelp));
            sender.sendMessage(AdventureUtil.toLegacy(helpHelp));
        }
    }
}