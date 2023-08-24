console.log("\n\n\nJS Loading...\n\n\n");

// For simplicity, we just use `Java.type(...)` to get Java classes.
// It's basically impossible to generate TypeScript types for the moment, although https://github.com/bensku/java-ts-bind exists 
// so if that could be interesting. We'll see.
const PlayerJoinEvent = Java.type("org.bukkit.event.player.PlayerJoinEvent");
const EventPriority = Java.type("org.bukkit.event.EventPriority");
const Script = Java.type("uwu.smsgamer.serverscripter.scripts.Script");
const ScriptCommand = Java.type("uwu.smsgamer.serverscripter.spigot.utils.ScriptCommand");
const ScriptListenerHelper = Java.type("uwu.smsgamer.serverscripter.spigot.utils.ScriptListenerHelper");

const TestCommand = {
    onTabComplete: (sender, command, alias, args) => {
        return ["JavaScript", "JS", "ECMAScript"];
    },
    onCommand: (sender, command, label, args) => {
        sender.sendMessage("Hello from JavaScript!");
        return true;
    }
}

function onJoin(event) {
    event.getPlayer().sendMessage("Hello World!");
}

function onEnable() {
    console.log("\n\n\nJS Enabled!\n\n\n");
    ScriptCommand.registerCommand(new ScriptCommand("gvmjstest", "GraalVM JavaScript Test", "/gvmjstest", [], TestCommand, TestCommand));
    ScriptListenerHelper.registerEvent(PlayerJoinEvent, EventPriority.NORMAL, onJoin, script);
}

function onDisable() {
    console.log("\n\n\nJS Disabled!\n\n\n");
}

function onReload() {
    console.log("\n\n\nJS Reloading!\n\n\n");
}
