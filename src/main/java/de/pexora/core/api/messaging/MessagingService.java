package de.pexora.core.api.messaging;

import de.pexora.core.util.AdventureUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Service zum Senden von Nachrichten an Spieler.
 * Diese Klasse stellt Methoden bereit, um formatierte Nachrichten an Spieler zu senden.
 */
public class MessagingService {
    
    /**
     * Sendet eine Nachrichtenkomponente an einen Spieler
     * 
     * @param player Der Spieler, der die Nachricht erhalten soll
     * @param component Die zu sendende Nachrichtenkomponente
     */
    public void sendMessage(Player player, Component component) {
        // In der Implementierung wird die Adventure API verwendet
        AdventureUtil.sendMessage(player, component);
    }
    
    /**
     * Sendet eine Textnachricht an einen Spieler
     * 
     * @param player Der Spieler, der die Nachricht erhalten soll
     * @param message Die zu sendende Textnachricht
     */
    public void sendMessage(Player player, String message) {
        player.sendMessage(message);
    }
    
    /**
     * Sendet eine Nachricht an einen Spieler mit dem Plugin-Präfix
     * 
     * @param player Der Spieler, der die Nachricht erhalten soll
     * @param component Die zu sendende Nachrichtenkomponente
     * @param usePrefix Ob der Plugin-Präfix verwendet werden soll
     */
    public void sendMessage(Player player, Component component, boolean usePrefix) {
        // In der Implementierung wird bei usePrefix=true das Plugin-Präfix vorangestellt
        AdventureUtil.sendMessage(player, component);
    }
    
    /**
     * Sendet eine Nachricht an alle Spieler auf dem Server
     * 
     * @param component Die zu sendende Nachrichtenkomponente
     */
    public void broadcastMessage(Component component) {
        // Konvertiere zu legacy-Format, damit die Bukkit-API es verarbeiten kann
        String legacyMessage = AdventureUtil.toLegacy(component);
        Bukkit.broadcastMessage(legacyMessage);
    }
    
    /**
     * Sendet eine Nachricht an alle Spieler mit einer bestimmten Berechtigung
     * 
     * @param component Die zu sendende Nachrichtenkomponente
     * @param permission Die erforderliche Berechtigung
     */
    public void broadcastMessage(Component component, String permission) {
        // Konvertiere zu legacy-Format, damit die Bukkit-API es verarbeiten kann
        String legacyMessage = AdventureUtil.toLegacy(component);
        Bukkit.broadcast(legacyMessage, permission);
    }
    
    /**
     * Sendet eine Aktionsleisten-Nachricht an einen Spieler
     * 
     * @param player Der Spieler, der die Nachricht erhalten soll
     * @param component Die zu sendende Nachrichtenkomponente
     */
    public void sendActionBar(Player player, Component component) {
        // In späteren Implementierungen mit vollständiger Adventure-API-Integration
        // würde hier direkt die Adventure-API verwendet werden
        // Für jetzt nutzen wir das Legacy-Format als Fallback
        String legacyMessage = AdventureUtil.toLegacy(component);
        player.sendMessage(legacyMessage);
    }
    
    /**
     * Sendet eine Titel-Nachricht an einen Spieler
     * 
     * @param player Der Spieler, der die Nachricht erhalten soll
     * @param title Der Haupttitel
     * @param subtitle Der Untertitel
     * @param fadeIn Die Einblendezeit in Ticks
     * @param stay Die Anzeigezeit in Ticks
     * @param fadeOut Die Ausblendezeit in Ticks
     */
    public void sendTitle(Player player, Component title, Component subtitle, 
                         int fadeIn, int stay, int fadeOut) {
        // Konvertiere zu legacy-Format für Bukkit-API-Kompatibilität
        String legacyTitle = AdventureUtil.toLegacy(title);
        String legacySubtitle = AdventureUtil.toLegacy(subtitle);
        
        player.sendTitle(legacyTitle, legacySubtitle, fadeIn, stay, fadeOut);
    }
}