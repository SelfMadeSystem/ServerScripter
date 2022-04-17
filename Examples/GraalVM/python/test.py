import java

PlayerJoinEvent = java.type('org.bukkit.event.player.PlayerJoinEvent')
EventPriority = java.type('org.bukkit.event.EventPriority')
Script = java.type('uwu.smsgamer.serverscripter.scripts.Script')
ScriptCommand = java.type('uwu.smsgamer.serverscripter.spigot.utils.ScriptCommand')
ScriptListenerHelper = java.type("uwu.smsgamer.serverscripter.spigot.utils.ScriptListenerHelper")


class TestCommand:  # Can't extend ScriptCommand because it's a foreign class
    def onCommand(self, sender, command, label, args):
        sender.sendMessage("Hello from GVM Python!")
        return True

    def onTabComplete(self, sender, command, alias, args):
        return ["python", "test"]


def on_join(event):
    event.getPlayer().sendMessage("Hello from GVM Python!")


def on_enable():
    tc = TestCommand()
    ScriptCommand.registerCommand(ScriptCommand("gvmpytest", "GraalVM Python Test", "/gvmpytest", [], tc, tc))
    ScriptListenerHelper.registerEvent(PlayerJoinEvent, EventPriority.NORMAL, on_join, script)
