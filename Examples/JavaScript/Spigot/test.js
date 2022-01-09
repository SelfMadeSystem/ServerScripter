// Author: Your Mom
// Version: 1.0
 // Description: This is a simple example of a plugin for the plugin system.
// Name: Test

print = function (a) {java.lang.System.out.println(a)}

SS = Packages.uwu.smsgamer.serverscripter;
SS_Spigot = SS.spigot;
events = Packages.org.bukkit.event
EventPriority = Packages.org.bukkit.event.EventPriority
Config = Packages.de.leonhard.storage.Config;
Sound = Packages.org.bukkit.Sound;

config = new Config(new Packages.java.io.File(SS.ScripterLoader.getInstance().getConfigDir(), "JSConfig.json"));
config.setDefault("JumpPads", [
    {
        material: "EMERALD_BLOCK",
        force: {
            h: 0.25,
            v: 2
        },
        sound: "ENTITY_ENDERMEN_TELEPORT",
        soundVolume: 1,
        soundPitch: 1,
        delay: 0.5,
    }
]);
config.write();

print("JavaScript Loading!!!!");

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

playerJumpTime = {}

onMove = {
    accept: function(event) {
         player = event.getPlayer();
         pos = player.getLocation();
         look = player.getLocation().getDirection();
         look.setY(0);
         if (look.length() > 0) look.normalize();
         under = pos.clone().subtract(0, 0.1, 0);
         block = player.getWorld().getBlockAt(under);
         blockType = block.getType();
         jumpPads = config.getList("JumpPads");
         for (i = 0; i < jumpPads.length; i++) {
             pad = jumpPads[i];
             if (blockType.name() == pad.material) {
                 now = Date.now();
                 if (!playerJumpTime[player.getUniqueId()]) {
                     playerJumpTime[player.getUniqueId()] = [];
                 } else {
                     last = playerJumpTime[player.getUniqueId()][i];
                     if (now - last < pad.delay * 1000) {
                         continue;
                     }
                 }
                 playerJumpTime[player.getUniqueId()][i] = now;
                 force = pad.force;
                 h = force.h;
                 v = force.v;
                 player.setVelocity(look.multiply(h).add(new org.bukkit.util.Vector(0, v, 0)));
                 player.playSound(player.getLocation(), Sound.valueOf(pad.sound), pad.soundVolume, pad.soundPitch);
             }
         }
    }
}

function onEnable() {
    print("JavaScript enabling!!!!!!!!")
    myCmd = new org.bukkit.command.TabExecutor(command)
    scmd = SS_Spigot.utils.ScriptCommand
    scmd.registerCommand(new scmd("jstest", "JavaScript Test", "/jstest", [], myCmd, myCmd))
    SS_Spigot.utils.ScriptListenerHelper.registerEvent(events.player.PlayerJoinEvent, EventPriority.NORMAL, onJoin, script)
    SS_Spigot.utils.ScriptListenerHelper.registerEvent(events.player.PlayerMoveEvent, EventPriority.NORMAL, onMove, script)
}

function onDisable() {
    print("JavaScript disabling!!!!!!!!")
//    SS_Spigot.utils.ScriptListenerHelper.unregisterEvent(events.player.PlayerJoinEvent, EventPriority.NORMAL, onJoin)
//    SS_Spigot.utils.ScriptListenerHelper.unregisterEvent(events.player.PlayerMoveEvent, EventPriority.NORMAL, onMove)
}

function onReload() {
    print("JavaScript reloading!!!!!!!!")
    config.forceReload();
}