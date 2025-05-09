package de.pexora.core.messaging;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.pexora.core.PexoraCore;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.nio.charset.StandardCharsets;

/**
 * Manages plugin messaging channel for Velocity/Proxy communication.
 */
public class PexoraChannel implements PluginMessageListener {

    private static final String CHANNEL_NAME = "pexora:core";
    
    private final PexoraCore plugin;
    
    public PexoraChannel(PexoraCore plugin) {
        this.plugin = plugin;
        
        // Register channel
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, CHANNEL_NAME);
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, CHANNEL_NAME, this);
        
        plugin.getLoggerService().info("Registered PexoraChannel '" + CHANNEL_NAME + "'");
    }
    
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals(CHANNEL_NAME)) {
            return;
        }
        
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subChannel = in.readUTF();
        
        switch (subChannel) {
            case "Handshake":
                handleHandshake(in);
                break;
            case "Status":
                handleStatusRequest(player, in);
                break;
            default:
                if (plugin.getCoreConfig().isDebugMode()) {
                    plugin.getLoggerService().info("[DEBUG] Received unknown subchannel: " + subChannel);
                }
        }
    }
    
    /**
     * Handles a handshake message from the proxy
     * 
     * @param in The input stream
     */
    private void handleHandshake(ByteArrayDataInput in) {
        String serverName = in.readUTF();
        String serverSecret = in.readUTF();
        
        // In a real implementation, you would validate the server secret
        // For now, we'll just log it
        plugin.getLoggerService().info("Received handshake from " + serverName);
        
        // Send a response to confirm the handshake
        if (plugin.getServer().getOnlinePlayers().isEmpty()) {
            plugin.getLoggerService().warn("Cannot respond to handshake: no online players");
            return;
        }
        
        Player player = plugin.getServer().getOnlinePlayers().iterator().next();
        sendHandshakeResponse(player, serverName);
    }
    
    /**
     * Handles a status request from the proxy
     * 
     * @param player The player that received the message
     * @param in The input stream
     */
    private void handleStatusRequest(Player player, ByteArrayDataInput in) {
        String requestId = in.readUTF();
        
        // Send back status information
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("StatusResponse");
        out.writeUTF(requestId);
        out.writeUTF(plugin.getDescription().getVersion());
        out.writeInt(plugin.getModuleLoader().getLoadedModules().size());
        
        player.sendPluginMessage(plugin, CHANNEL_NAME, out.toByteArray());
    }
    
    /**
     * Sends a handshake response to the proxy
     * 
     * @param player The player to send through
     * @param serverName The server name
     */
    private void sendHandshakeResponse(Player player, String serverName) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("HandshakeResponse");
        out.writeUTF(serverName);
        out.writeUTF(plugin.getDescription().getVersion());
        
        player.sendPluginMessage(plugin, CHANNEL_NAME, out.toByteArray());
    }
    
    /**
     * Sends a message to the proxy
     * 
     * @param player The player to send through
     * @param subChannel The subchannel
     * @param data The data to send
     */
    public void sendMessage(Player player, String subChannel, String... data) {
        if (player == null) {
            plugin.getLoggerService().warn("Cannot send message: player is null");
            return;
        }
        
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(subChannel);
        
        for (String datum : data) {
            out.writeUTF(datum);
        }
        
        player.sendPluginMessage(plugin, CHANNEL_NAME, out.toByteArray());
    }
    
    /**
     * Unregisters the channel
     */
    public void unregister() {
        plugin.getServer().getMessenger().unregisterOutgoingPluginChannel(plugin, CHANNEL_NAME);
        plugin.getServer().getMessenger().unregisterIncomingPluginChannel(plugin, CHANNEL_NAME, this);
    }
}
