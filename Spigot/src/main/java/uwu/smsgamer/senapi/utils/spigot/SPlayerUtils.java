/*
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package uwu.smsgamer.senapi.utils.spigot;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permissible;
import uwu.smsgamer.serverscripter.senapi.Constants;

/**
 * Utilities regarding players.
 */
public class SPlayerUtils {
    /**
     * Returns an array of characters representing every colour the player has access to.
     *
     * @param permissionBase The permission base to use when checking their permission.
     * @param permissible    The {@link Permissible} to check the permission of.
     * @return an array of characters representing every colour the player has access to.
     */
    public static char[] getAllowedColors(String permissionBase, Permissible permissible) {
        char[] allowedColors = new char[0];
        for (char c : Constants.getCharColors())
            if (permissible.hasPermission(permissionBase + c)) {
                char[] chars = new char[allowedColors.length + 1];
                System.arraycopy(allowedColors, 0, chars, 0, allowedColors.length);
                chars[chars.length - 1] = c;
                allowedColors = chars;
            }
        return allowedColors;
    }

    /**
     * Returns the sender cast to an offline player if it's an offline players.
     * If it isn't, then it returns a {@link SConsolePlayer} instance.
     *
     * @param sender The sender to check for.
     * @return the sender cast to an offline player if it's an offline players.
     * If it isn't, then it returns a {@link SConsolePlayer} instance.
     */
    public static OfflinePlayer getPlayer(CommandSender sender) {
        if (sender instanceof OfflinePlayer) {
            return (OfflinePlayer) sender;
        } else return SConsolePlayer.getInstance();
    }
}
