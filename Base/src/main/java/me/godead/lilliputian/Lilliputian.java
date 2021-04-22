/*
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
 */

/*
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package me.godead.lilliputian;

import org.jetbrains.annotations.*;
import uwu.smsgamer.senapi.Loader;

public class Lilliputian {

    @Nullable
    private static Loader plugin = null;
    @Nullable
    private static String path = null;
    private final DependencyBuilder builder = new DependencyBuilder();

    /**
     * @param plugin Your plugin instance
     */
    public Lilliputian(@NotNull Loader plugin) {
        Lilliputian.plugin = plugin;
        path = plugin.getDataFolder().getParent() + "/" + "LilliputianLibraries";
    }

    /**
     * @param plugin Your plugin instance
     * @param path   (Optional) The path to download the Dependencies to.
     *               (Example: "/MyPlugin" Will download them to a Folder called MyPlugin inside the server's plugins folder)
     */
    public Lilliputian(@NotNull Loader plugin, String path) {
        Lilliputian.plugin = plugin;
        Lilliputian.path = plugin.getDataFolder().getParent() + path;
    }

    @NotNull
    public static Loader getPlugin() {
        assert plugin != null : "Error. Plugin seems to be null";
        return plugin;
    }

    @NotNull
    public static String getPath() {
        assert path != null : "Error. Path seems to be null";
        return path;
    }

    public DependencyBuilder getDependencyBuilder() {
        return builder;
    }
}
