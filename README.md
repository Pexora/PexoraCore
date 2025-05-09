# PexoraCore 

Ein modulares Minecraft-Plugin-Core-System, das zentralisierte Modulverwaltung, Konfiguration, Messaging und API-Funktionalität für Minecraft-Server bietet.

## Übersicht

PexoraCore ist ein modulares Plugin-System für Minecraft-Server, das Folgendes ermöglicht:

- Automatisches Laden und Verwalten von Modul-Plugins
- Zentralisierte Konfigurationsverwaltung
- Einheitliches Nachrichtensystem mit MiniMessage-Unterstützung
- Integriertes Logging mit einheitlicher Formatierung
- Server-übergreifende Kommunikation über Plugin-Messaging-Kanäle
- Statusüberwachung aller Module
- PlaceholderAPI-Integration

## Funktionen

### 📁 Modul-System (ModuleLoader)
- Lädt automatisch alle Module aus dem Unterverzeichnis `/modules`
- Unterstützt Subplugins im Format PexoraXYZ.jar
- Jedes Modul wird wie ein Plugin behandelt (JavaPlugin)
- Abhängigkeit zu PexoraCore wird geprüft

### 💬 MessageConfig (messages.yml)
- Lädt benutzerdefinierte Nachrichten (Fehler, Systemmeldungen)
- Ersetzt Platzhalter wie %prefix%
- Nutzt MiniMessage (Adventure) zur Farb- und Formatkontrolle
- Unterstützt Live-Aktualisierung für Updates

### ⚙️ CoreConfig (config.yml)
- Zentrale Konfigurationsdatei für systemweite Optionen
- Konfigurierbarer Debug-Modus
- Modul-Auto-Reload-Option
- Datenbankeinstellungen (für zukünftige Verwendung)
- Live aktualisierbar

### 🧩 PlaceholderAPI-Erkennung
- Prüft beim Start auf PlaceholderAPI
- Gibt Warnung aus, wenn nicht installiert
- Macht Verfügbarkeitsstatus für Module zugänglich

### 🔗 Proxy-Handshake (PexoraChannel)
- Unabhängiger Plugin-Nachrichten-Kanal (pexora:core)
- Wird für Velocity/Proxy-Kommunikation verwendet
- Unterstützt Authentifizierung und Statusübertragung (Handshake)
- Vorbereitung für: PlayerSync, DatenbankSync, Messaging

### 📡 StatusAPI
- Registriert alle laufenden Module im Speicher
- Verwaltet Karte mit Ladezuständen
- Nutzbar für /pexora status oder Web-Panel-Integration

### 🎨 [PX]-Prefix
- Anpassbar über messages.yml
- Standard: `<gradient:#ff55ff:#aa00ff>[PX]</gradient>`
- Kann in jeder Nachricht mit %prefix% verwendet werden
- Verfügbar als Component

### 🧾 Logging / LoggerService
- Saubere Ausgaben mit [PX]-Prefix im Log
- Unterstützt: INFO, WARN, ERROR
- Optional farbig im Konsolelog (Spigot-kompatibel)

### 🧠 API für andere Pexora-Plugins
- `PexoraAPI.get()` liefert Singleton-Instanz
- Bietet Zugriff auf:
  - Konfigurationen
  - Logger
  - PlaceholderAPI-Prüfung
  - Registrierte Module
  - Nachrichten-Komponenten

### 🧪 Debug-Modus
- Konfigurierbar über config.yml
- Aktiviert zusätzliche Konsolenlogs
- Hilfreich für Entwicklung und Fehlersuche

## Installation

1. Lade die neueste PexoraCore.jar von der Releases-Seite herunter
2. Platziere sie im plugins-Verzeichnis deines Servers
3. Starte deinen Server - dies generiert die Konfigurationsdateien
4. Passe deine Konfiguration nach Bedarf an
5. Platziere Modul-Plugins im Verzeichnis `plugins/PexoraCore/modules`

## Erstellen eines Moduls

Module sind Plugins, die von PexoraCore abhängen. Um ein Modul zu erstellen:

1. Erstelle ein standardmäßiges Bukkit/Spigot-Plugin
2. Mache PexoraCore zu einer Abhängigkeit, indem du es zu deiner plugin.yml hinzufügst:
   ```yaml
   depend: [PexoraCore]
   ```
3. Benenne dein Plugin mit dem "Pexora"-Präfix (z.B. PexoraEconomy)
4. Verwende die PexoraAPI in deinem Plugin:
   ```java
   import de.pexora.core.api.PexoraAPI;
   
   // API-Instanz holen
   PexoraAPI api = PexoraAPI.get();
   
   // Eine Nachricht loggen
   api.info("Hallo von meinem Modul!");
   
   // Eine Nachricht vom Core holen
   Component message = api.getMessage("some-message-key");
   ```

## Befehle

- `/pexora reload` - Lädt das Plugin und alle Module neu
- `/pexora status` - Zeigt den Status des Plugins und aller Module
- `/pexora help` - Zeigt das Hilfemenü

## Berechtigungen

- `pexora.admin` - Erlaubt Zugriff auf alle PexoraCore-Befehle

## Konfigurationsdateien

### config.yml
```yaml
# Detaillierte Debug-Ausgabe in der Konsole aktivieren
debug-mode: false

# Module automatisch neu laden, wenn der Core neu geladen wird
module-auto-reload: true

# Datenbankeinstellungen (für zukünftige Verwendung)
database:
  enabled: false
  host: localhost
  port: 3306
  name: pexora
  user: root
  password: ""
```

### messages.yml
```yaml
# Das Präfix für alle Nachrichten
prefix: "<gradient:#ff55ff:#aa00ff>[PX]</gradient> "

# Allgemeine Nachrichten
plugin-enabled: "%prefix% <green>PexoraCore wurde aktiviert!"
plugin-disabled: "%prefix% <red>PexoraCore wurde deaktiviert!"
plugin-reloaded: "%prefix% <green>PexoraCore wurde neu geladen!"

# Füge hier deine eigenen Nachrichten hinzu
```

## Beispielmodul

Siehe das Verzeichnis `example-modules` für ein vollständiges Beispiel eines Moduls, das PexoraCore verwendet.