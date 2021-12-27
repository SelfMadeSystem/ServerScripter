/*
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package uwu.smsgamer.serverscripter.spigot.senapi.spigot;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.*;

/**
 * Spigot string utils. Utilities for strings that require the Spigot API.
 */
public class SStringUtils {
    /**
     * If PlaceholderAPI is enabled, it uses PlaceholderAPI for placeholder replacement.
     * If not, it simply replaces {@code %player_name%} with the player's name.
     *
     * @param player The player for replacements.
     * @param string The string with the placeholders.
     * @return A string with placeholders replaced.
     */
    public static String replacePlaceholders(final OfflinePlayer player, final String string) {
        if (SPluginUtils.isPlaceholderAPIEnabled()) {
            if (player instanceof SConsolePlayer)
                Bukkit.getLogger().warning(
                        "If an error occurs, please do not report Sms_Gamer or a placeholder's author. " +
                                "The player parameter was a ConsolePlayer.");
            return PlaceholderAPI.setPlaceholders(player, string);
        } else return string.replace("%player_name%", player.getName());
    }
}
