/* Name: RunOnly
*  Description: You can only run! No walking!
 *  Version: 1.0
   Author: Yer Mum
*/

print = function (a) {java.lang.System.out.println(a)}

SS = Packages.uwu.smsgamer.serverscripter;
SS_Spigot = SS.spigot;
events = Packages.org.bukkit.event
EventPriority = Packages.org.bukkit.event.EventPriority

print("JavaScript Loading!!!!");

sprintOnly = {}

command = {
    onTabComplete: function(sender, command, alias, args) {
        return ["JavaScript", "JS", "ECMAScript"]
    },
    onCommand: function(sender, command, label, args) {
        if (sprintOnly[sender]){
            sprintOnly[sender] = false;
            sender.sendMessage("Disabled");
        } else {
            sprintOnly[sender] = sender.getLocation();
            sender.sendMessage("Enabled");
        }
    }
}

onMove = {
    accept: function(event) {
         player = event.getPlayer();
         if (sprintOnly[player] && !player.isSprinting()) {
             player.teleport(sprintOnly[player]);
             player.sendMessage("You should be sprinting!");
         }
    }
}

function onEnable() {
    print("JavaScript enabling!!!!!!!!")
    myCmd = new org.bukkit.command.TabExecutor(command)
    scmd = SS_Spigot.utils.ScriptCommand
    scmd.registerCommand(new scmd("runonly", "ARSRAS", "/runonly", [], myCmd, myCmd))
    SS_Spigot.utils.ScriptListenerHelper.registerEvent(events.player.PlayerMoveEvent, EventPriority.NORMAL, onMove, script)
}

function onDisable() {
    print("JavaScript disabling!!!!!!!!")
}

function onReload() {
    print("JavaScript reloading!!!!!!!!")
    config.forceReload();
}