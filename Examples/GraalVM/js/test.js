console.log("\n\n\nJS Loading...\n\n\n");

let PlayerJoinEvent = Java.type("org.bukkit.event.player.PlayerJoinEvent");
let EventPriority = Java.type("org.bukkit.event.EventPriority");
let Script = Java.type("uwu.smsgamer.serverscripter.scripts.Script");
let ScriptCommand = Java.type("uwu.smsgamer.serverscripter.spigot.utils.ScriptCommand");
let ScriptListenerHelper = Java.type("uwu.smsgamer.serverscripter.spigot.utils.ScriptListenerHelper");

var TestCommand = {
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