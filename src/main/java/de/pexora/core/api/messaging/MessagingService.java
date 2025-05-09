package de.pexora.core.api.messaging;

import de.pexora.core.util.AdventureUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

/**
 * Service zum Senden von Nachrichten an Spieler.
 * Diese Klasse stellt Methoden bereit, um formatierte Nachrichten an Spieler zu senden.
 */
public class MessagingService {

    /**
     * Konstruktor für den MessagingService
     */
    public MessagingService() {
        // Leerer Konstruktor
    }
    
    /**
     * Sendet eine Nachricht an einen Spieler
     * 
     * @param player Der Spieler, an den die Nachricht gesendet wird
     * @param message Die zu sendende Nachricht als Component
     */
    public void sendMessage(Player player, Component message) {
        AdventureUtil.sendMessage(player, message);
    }
    
    /**
     * Sendet eine Nachricht an einen Spieler
     * 
     * @param player Der Spieler, an den die Nachricht gesendet wird
     * @param message Die zu sendende Nachricht als MiniMessage-String
     */
    public void sendMessage(Player player, String message) {
        AdventureUtil.sendMessage(player, message);
    }
    
    /**
     * Sendet eine ActionBar-Nachricht an einen Spieler
     * 
     * @param player Der Spieler, an den die Nachricht gesendet wird
     * @param message Die zu sendende Nachricht als Component
     */
    public void sendActionBar(Player player, Component message) {
        player.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR, 
                net.md_5.bungee.api.chat.TextComponent.fromLegacyText(AdventureUtil.toLegacy(message)));
    }
    
    /**
     * Sendet eine ActionBar-Nachricht an einen Spieler
     * 
     * @param player Der Spieler, an den die Nachricht gesendet wird
     * @param message Die zu sendende Nachricht als MiniMessage-String
     */
    public void sendActionBar(Player player, String message) {
        sendActionBar(player, AdventureUtil.parse(message));
    }
    
    /**
     * Sendet eine Titel-Nachricht an einen Spieler
     * 
     * @param player Der Spieler, an den die Nachricht gesendet wird
     * @param title Der Haupttitel als Component
     * @param subtitle Der Untertitel als Component
     * @param fadeIn Die Einblendezeit in Ticks
     * @param stay Die Anzeigezeit in Ticks
     * @param fadeOut Die Ausblendezeit in Ticks
     */
    public void sendTitle(Player player, Component title, Component subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(
                AdventureUtil.toLegacy(title), 
                AdventureUtil.toLegacy(subtitle), 
                fadeIn, stay, fadeOut
        );
    }
    
    /**
     * Sendet eine Titel-Nachricht an einen Spieler
     * 
     * @param player Der Spieler, an den die Nachricht gesendet wird
     * @param title Der Haupttitel als MiniMessage-String
     * @param subtitle Der Untertitel als MiniMessage-String
     * @param fadeIn Die Einblendezeit in Ticks
     * @param stay Die Anzeigezeit in Ticks
     * @param fadeOut Die Ausblendezeit in Ticks
     */
    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        sendTitle(
                player, 
                AdventureUtil.parse(title), 
                AdventureUtil.parse(subtitle), 
                fadeIn, stay, fadeOut
        );
    }
}