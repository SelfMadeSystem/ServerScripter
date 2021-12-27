/*
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package uwu.smsgamer.serverscripter.spigot.senapi.spigot;

import org.bukkit.Bukkit;

/**
 * Utils for plugins.
 */
public class SPluginUtils {
    /**
     * Gets if a plugin is enabled.
     *
     * @param name Plugin name.
     * @return If the plugin is enabled.
     */
    public static boolean isPluginEnabled(String name) {
        return Bukkit.getPluginManager().isPluginEnabled(name);
    }

    /**
     * Returns if PlaceholderAPI is enabled.
     *
     * @return If PlaceholderAPI is enabled.
     */
    public static boolean isPlaceholderAPIEnabled() {
        return isPluginEnabled("PlaceholderAPI");
    }
}
