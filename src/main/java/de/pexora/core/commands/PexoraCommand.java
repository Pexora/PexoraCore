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
 * Handles the main PexoraCore command and its subcommands.
 */
public class PexoraCommand implements CommandExecutor {

    private final PexoraCore plugin;

    public PexoraCommand(PexoraCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if the command is /pexora
        if (!command.getName().equalsIgnoreCase("pexora")) {
            return false;
        }

        // Check permission
        if (!sender.hasPermission("pexora.admin")) {
            plugin.getMessageConfig().getMessage("error-command-permission");
            return true;
        }

        // Handle subcommands
        if (args.length == 0) {
            // No subcommand provided, show help
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
                // Unknown subcommand
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
        // Reload the plugin
        plugin.reload();
        
        // Send confirmation message
        Component message = plugin.getMessageConfig().getMessage("plugin-reloaded");
        if (sender instanceof Player) {
            AdventureUtil.sendMessage((Player) sender, message);
        } else {
            sender.sendMessage(AdventureUtil.toLegacy(message));
        }
    }

    private void handleStatus(CommandSender sender) {
        // Get plugin version
        String version = plugin.getDescription().getVersion();
        
        // Get module status
        Map<String, Boolean> moduleStatus = plugin.getStatusAPI().getModuleStatus();
        int enabledCount = plugin.getStatusAPI().getEnabledModuleCount();
        int totalCount = plugin.getStatusAPI().getTotalModuleCount();
        
        // Send header
        Component header = plugin.getMessageConfig().getMessage("status-header");
        Component versionInfo = plugin.getMessageConfig().getMessage("status-version", "version", version);
        Component moduleCountInfo = plugin.getMessageConfig().getMessage("status-modules", "count", enabledCount + "/" + totalCount);
        
        // Send PlaceholderAPI status
        String papiStatus = plugin.isPlaceholderApiAvailable() ? "§aInstalled" : "§cNot installed";
        Component papiInfo = plugin.getMessageConfig().getMessage("status-placeholderapi", "status", papiStatus);
        
        // Send messages
        if (sender instanceof Player) {
            Player player = (Player) sender;
            AdventureUtil.sendMessage(player, header);
            AdventureUtil.sendMessage(player, versionInfo);
            AdventureUtil.sendMessage(player, moduleCountInfo);
            AdventureUtil.sendMessage(player, papiInfo);
            
            // Send module list
            for (Map.Entry<String, Boolean> entry : moduleStatus.entrySet()) {
                String moduleStatusText = entry.getValue() ? "§aEnabled" : "§cDisabled";
                Component moduleEntry = plugin.getMessageConfig().getMessage("status-module-entry", 
                        "module", entry.getKey(), 
                        "status", moduleStatusText);
                AdventureUtil.sendMessage(player, moduleEntry);
            }
        } else {
            // Console sender
            sender.sendMessage(AdventureUtil.toLegacy(header));
            sender.sendMessage(AdventureUtil.toLegacy(versionInfo));
            sender.sendMessage(AdventureUtil.toLegacy(moduleCountInfo));
            sender.sendMessage(AdventureUtil.toLegacy(papiInfo));
            
            // Send module list
            for (Map.Entry<String, Boolean> entry : moduleStatus.entrySet()) {
                String moduleStatusText = entry.getValue() ? "Enabled" : "Disabled";
                Component moduleEntry = plugin.getMessageConfig().getMessage("status-module-entry", 
                        "module", entry.getKey(), 
                        "status", moduleStatusText);
                sender.sendMessage(AdventureUtil.toLegacy(moduleEntry));
            }
        }
    }

    private void showHelp(CommandSender sender) {
        // Get messages
        Component header = plugin.getMessageConfig().getMessage("help-header");
        Component reloadHelp = plugin.getMessageConfig().getMessage("help-command-reload");
        Component statusHelp = plugin.getMessageConfig().getMessage("help-command-status");
        Component helpHelp = plugin.getMessageConfig().getMessage("help-command-help");
        
        // Send messages
        if (sender instanceof Player) {
            Player player = (Player) sender;
            AdventureUtil.sendMessage(player, header);
            AdventureUtil.sendMessage(player, reloadHelp);
            AdventureUtil.sendMessage(player, statusHelp);
            AdventureUtil.sendMessage(player, helpHelp);
        } else {
            // Console sender
            sender.sendMessage(AdventureUtil.toLegacy(header));
            sender.sendMessage(AdventureUtil.toLegacy(reloadHelp));
            sender.sendMessage(AdventureUtil.toLegacy(statusHelp));
            sender.sendMessage(AdventureUtil.toLegacy(helpHelp));
        }
    }
}