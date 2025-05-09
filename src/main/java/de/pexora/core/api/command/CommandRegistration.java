package de.pexora.core.api.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;

/**
 * Hilfsklasse zur Registrierung von Befehlen im PexoraCore-System.
 * Diese Klasse erleichtert das Registrieren und Verwalten von Befehlen für Module.
 */
public class CommandRegistration {
    
    private final String commandName;
    private final CommandExecutor executor;
    private TabCompleter tabCompleter;
    private String[] aliases;
    private String permission;
    private String description;
    private String usage;
    
    /**
     * Erstellt eine neue CommandRegistration-Instanz
     * 
     * @param commandName Der Name des Befehls
     * @param executor Der Befehlsausführer
     */
    public CommandRegistration(String commandName, CommandExecutor executor) {
        this.commandName = commandName;
        this.executor = executor;
    }
    
    /**
     * Setzt den TabCompleter für den Befehl
     * 
     * @param tabCompleter Der TabCompleter
     * @return Diese CommandRegistration-Instanz (für Method-Chaining)
     */
    public CommandRegistration withTabCompleter(TabCompleter tabCompleter) {
        this.tabCompleter = tabCompleter;
        return this;
    }
    
    /**
     * Setzt die Aliase für den Befehl
     * 
     * @param aliases Die Befehlsaliase
     * @return Diese CommandRegistration-Instanz (für Method-Chaining)
     */
    public CommandRegistration withAliases(String... aliases) {
        this.aliases = aliases;
        return this;
    }
    
    /**
     * Setzt die erforderliche Berechtigung für den Befehl
     * 
     * @param permission Die Befehlsberechtigung
     * @return Diese CommandRegistration-Instanz (für Method-Chaining)
     */
    public CommandRegistration withPermission(String permission) {
        this.permission = permission;
        return this;
    }
    
    /**
     * Setzt die Beschreibung des Befehls
     * 
     * @param description Die Befehlsbeschreibung
     * @return Diese CommandRegistration-Instanz (für Method-Chaining)
     */
    public CommandRegistration withDescription(String description) {
        this.description = description;
        return this;
    }
    
    /**
     * Setzt die Verwendungshinweise für den Befehl
     * 
     * @param usage Die Verwendungshinweise
     * @return Diese CommandRegistration-Instanz (für Method-Chaining)
     */
    public CommandRegistration withUsage(String usage) {
        this.usage = usage;
        return this;
    }
    
    /**
     * Gibt den Befehlsnamen zurück
     * 
     * @return Der Befehlsname
     */
    public String getCommandName() {
        return commandName;
    }
    
    /**
     * Gibt den Befehlsausführer zurück
     * 
     * @return Der Befehlsausführer
     */
    public CommandExecutor getExecutor() {
        return executor;
    }
    
    /**
     * Gibt den TabCompleter zurück
     * 
     * @return Der TabCompleter oder null
     */
    public TabCompleter getTabCompleter() {
        return tabCompleter;
    }
    
    /**
     * Gibt die Befehlsaliase zurück
     * 
     * @return Die Befehlsaliase oder null
     */
    public String[] getAliases() {
        return aliases;
    }
    
    /**
     * Gibt die Befehlsberechtigung zurück
     * 
     * @return Die Befehlsberechtigung oder null
     */
    public String getPermission() {
        return permission;
    }
    
    /**
     * Gibt die Befehlsbeschreibung zurück
     * 
     * @return Die Befehlsbeschreibung oder null
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Gibt die Verwendungshinweise zurück
     * 
     * @return Die Verwendungshinweise oder null
     */
    public String getUsage() {
        return usage;
    }
}