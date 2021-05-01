print = function (a) {java.lang.System.out.println(a)}

print("JavaScript Loading!!!!")

command = {
    onTabComplete: function(sender, command, alias, args) {
        return ["JavaScript", "JS", "ECMAScript"]
    },
    onCommand: function(sender, command, label, args) {
        sender.sendMessage("Hello from JavaScript!")
    }
}

function onEnable() {
    print("JavaScript enabling!!!!!!!!")
    myCmd = new org.bukkit.command.TabExecutor(command)
    scmd = Packages.uwu.smsgamer.serverscripter.spigot.utils.ScriptCommand
    scmd.registerCommand(new scmd("jstest", "JavaScript Test", "/jstest", [], myCmd, myCmd))
}

function onDisable() {
    print("JavaScript disabling!!!!!!!!")
}

function onReload() {
    print("JavaScript reloading!!!!!!!!")
}