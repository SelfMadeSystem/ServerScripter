![Logo](https://raw.githubusercontent.com/True-cc/ServerScripter/master/.idea/icon.png)
# ServerScripter
Multi Language Scripting Plugin for Minecraft Servers.

## Languages
- JavaScript (Rhino)
- Python (Jython)
- Groovy (Groovy)
- JavaScript (GraalJS)
- Python (GraalPython)
- R (GraalVM R)

Using the plugin system, you can easily add your own languages.

## Supported Platforms
- Spigot / PaperSpigot / Bukkit / CraftBukkit
- BungeeCord / Waterfall
- Velocity

Because of the modular design, it is easy to add support for other platforms. In fact, it doesn't even have to be a Minecraft server. Any Java application with a plugin system can be supported.

## Other features
- Basic command support for spigot and bungee
- Basic event support for spigot and bungee
- [PacketEvents](https://github.com/retrooper/packetevents) support for spigot (plugin required)

## Installation

The latest version is not yet released, so you have to build it yourself.

### Build

1. Clone the repository

```bash
git clone https://github.com/SelfMadeSystem/ServerScripter.git
```

2. Build the project

```bash
cd ServerScripter
./gradlew build
```

3. Copy the built jar for either the proxy or the server to the plugins folder

4. Run the server once to generate the config files and the addons folder

5. Copy the built jar for the languages you want to use to the addons folder

## Examples

You may view examples for each language in the [examples](https://github.com/SelfMadeSystem/ServerScripter/tree/master/Examples) folder.
