from com.velocitypowered.api.event import EventHandler
from com.velocitypowered.api.event.proxy import ProxyPingEvent
from uwu.smsgamer.serverscripter.velocity import VelocityServerScripter
from com.velocitypowered.api.command import SimpleCommand
from net.kyori.adventure.text import Component
from net.kyori.adventure.text.format import NamedTextColor

print("Hello")


class ListenerTest(EventHandler):
    def execute(self, e):
        print("Pyng: " + str(e))


class TestCommand(SimpleCommand):
    def execute(self, invocation):
        invocation.source().sendMessage(Component.text("Hello world!").color(NamedTextColor.BLUE))


def on_enable():
    print("On Enable!")
    ins = VelocityServerScripter.getInstance()
    srv = ins.getServer()
    srv.getEventManager().register(ins, ProxyPingEvent, ListenerTest())
    srv.getCommandManager().register("pycmdtest", TestCommand())


def on_disable():
    print("On Disable!")


def on_reload():
    print("On Reload!")
