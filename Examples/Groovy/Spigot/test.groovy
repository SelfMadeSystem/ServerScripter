import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerJoinEvent
import uwu.smsgamer.serverscripter.groovy.scripts.GrScriptLoader
import uwu.smsgamer.serverscripter.spigot.utils.ScriptCommand
import uwu.smsgamer.serverscripter.spigot.utils.ScriptListenerHelper

import java.util.function.Consumer


var shell = new GroovyShell()

var f2 = new File(GrScriptLoader.getInstance().getScriptDirectory(), "_test2.groovy")

def test2 = shell.parse(f2)

test2.test()


println("Groovy script Loaded.")


class CommandTest implements TabExecutor {
    boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("OwO Noice")
        return true;
    }

    List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return ["Hi", "Hewwo"]
    }
}

class ListenerTest implements Consumer<PlayerJoinEvent> {
    void accept(PlayerJoinEvent t) {
        t.getPlayer().sendMessage("Hewwo fwom gwoovy~ c:")
    }
}

void onEnable() {
    println("Groovy script Enabled")
    def cmd = new CommandTest()
    ScriptCommand.registerCommand(new ScriptCommand("groovytest", "Groovy Testing", "/groovytest", [], cmd, cmd))
    ScriptListenerHelper.registerEvent(PlayerJoinEvent.class, EventPriority.NORMAL, new ListenerTest())
}

def onDisable() {
    println("Groovy script Disabled")
}

def onReload() {
    println("Groovy script Reloaded")
}
