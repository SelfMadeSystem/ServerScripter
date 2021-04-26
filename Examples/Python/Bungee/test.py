from net.md_5.bungee.api import ProxyServer
from net.md_5.bungee.api import ServerPing
from net.md_5.bungee.api.event import ProxyPingEvent
from net.md_5.bungee.api.chat import ComponentBuilder
from net.md_5.bungee.api.plugin import Command
from uwu.smsgamer.serverscripter.bungee import BungeeServerScripter
from uwu.smsgamer.serverscripter.bungee.utils import ScriptListenerHelper
from java.util.function import Consumer

print("Hi")


class MyCommand(Command):
    def __init__(self):
        super(MyCommand, self).__init__("pybungeetest")

    def execute(self, sender, args):
        sender.sendMessage(ComponentBuilder("Hi").create())


class ListenerTest(Consumer):
    def accept(self, e):
        players = ProxyPingEvent.getResponse(e).getPlayers()
        players.setMax(-1)
        players.setOnline(3)
        players.setSample(ServerPing.PlayerInfo)


def on_enable():
    print("Enable")
    ProxyServer.getInstance().getPluginManager().registerCommand(BungeeServerScripter.getInstance(), MyCommand())
    ScriptListenerHelper.registerListener(ProxyPingEvent, ListenerTest())


def on_disable():
    print("Disable")


def on_reload():
    print("Reload")
