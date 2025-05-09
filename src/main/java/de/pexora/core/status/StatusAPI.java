package de.pexora.core.status;

import java.util.HashMap;
import java.util.Map;

/**
 * Tracks the status of all modules in the system.
 */
public class StatusAPI {

    private final Map<String, Boolean> moduleStatus;
    
    public StatusAPI() {
        this.moduleStatus = new HashMap<>();
    }
    
    /**
     * Registers a module with its enabled status
     * 
     * @param moduleName The name of the module
     * @param enabled Whether the module is enabled
     */
    public void registerModule(String moduleName, boolean enabled) {
        moduleStatus.put(moduleName, enabled);
    }
    
    /**
     * Unregisters a module
     * 
     * @param moduleName The name of the module
     */
    public void unregisterModule(String moduleName) {
        moduleStatus.remove(moduleName);
    }
    
    /**
     * Checks if a module is enabled
     * 
     * @param moduleName The name of the module
     * @return Whether the module is enabled, or false if not found
     */
    public boolean isModuleEnabled(String moduleName) {
        return moduleStatus.getOrDefault(moduleName, false);
    }
    
    /**
     * Gets the status of all modules
     * 
     * @return A map of module names to their enabled status
     */
    public Map<String, Boolean> getModuleStatus() {
        return new HashMap<>(moduleStatus);
    }
    
    /**
     * Gets the count of enabled modules
     * 
     * @return The number of enabled modules
     */
    public int getEnabledModuleCount() {
        int count = 0;
        for (boolean enabled : moduleStatus.values()) {
            if (enabled) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Gets the total count of registered modules
     * 
     * @return The total number of modules
     */
    public int getTotalModuleCount() {
        return moduleStatus.size();
    }
}
