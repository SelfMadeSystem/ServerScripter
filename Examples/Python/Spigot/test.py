from uwu.smsgamer.serverscripter.spigot.utils import ScriptCommand
from uwu.smsgamer.serverscripter.spigot.utils import ScriptListenerHelper
from org.bukkit.command import TabExecutor
from org.bukkit.event.player import PlayerJoinEvent
from org.bukkit.event import EventPriority
from java.util.function import Consumer

import _test2 as t2

print t2.test_thing


print("On Load!!! Name: " + __name__)


class CommandTest(TabExecutor):
    def onTabComplete(self, sender, command, alias, args):
        return ["Hello", "Hi"]

    def onCommand(self, sender, command, label, args):
        sender.sendMessage("Hi")


class JoinListener(Consumer):
    def accept(self, t):
        t.getPlayer().sendMessage("Hewwo!!!!")


def on_enable():
    print("On Enable!!!")
    cmd = CommandTest()
    ScriptCommand.registerCommand(ScriptCommand("pytest", "Python Testing", "/test", [], cmd, cmd))
    ScriptListenerHelper.registerEvent(PlayerJoinEvent, EventPriority.NORMAL, JoinListener())
    import _test2 as t
    print(t.test_thing)


def on_disable():
    print("On Disable!!!")


def on_reload():
    print("On Reload!!!")
    import _test2 as t
    reload(t)
    print(t.test_thing)
