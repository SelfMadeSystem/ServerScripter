print = function (a) {java.lang.System.out.println(a)}

SS_Spigot = Packages.uwu.smsgamer.serverscripter.spigot;
PlayerJoinEvent = Packages.org.bukkit.event.player.PlayerJoinEvent
EventPriority = Packages.org.bukkit.event.EventPriority

print("JavaScript Loading!!!!")

command = {
    onTabComplete: function(sender, command, alias, args) {
        return ["JavaScript", "JS", "ECMAScript"]
    },
    onCommand: function(sender, command, label, args) {
        sender.sendMessage("Hello from JavaScript!")
    }
}

onJoin = {
    accept: function(event) {
        event.getPlayer().sendMessage("Hello from JavaScript!!!")
    }
}

function onEnable() {
    print("JavaScript enabling!!!!!!!!")
    myCmd = new org.bukkit.command.TabExecutor(command)
    scmd = SS_Spigot.utils.ScriptCommand
    scmd.registerCommand(new scmd("jstest", "JavaScript Test", "/jstest", [], myCmd, myCmd))
    SS_Spigot.utils.ScriptListenerHelper.registerEvent(PlayerJoinEvent, EventPriority.NORMAL, onJoin)
}

function onDisable() {
    print("JavaScript disabling!!!!!!!!")
}

function onReload() {
    print("JavaScript reloading!!!!!!!!")
}