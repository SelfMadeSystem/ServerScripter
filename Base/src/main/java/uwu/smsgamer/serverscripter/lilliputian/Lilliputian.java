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

package uwu.smsgamer.serverscripter.lilliputian;

import lombok.Getter;
import org.jetbrains.annotations.*;
import uwu.smsgamer.serverscripter.senapi.Loader;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class Lilliputian {

    @Nullable
    private static Loader plugin = null;
    @Nullable
    private static String path = null;
    @Getter
    private static CustomClassLoader classLoader;
    @Getter
    private static DependencyLoader loader;

    public static void load(Loader pl) {
        plugin = pl;
        classLoader = new CustomClassLoader(new URL[0], plugin.getClass().getClassLoader());
        loader = new DependencyLoader(classLoader);
    }
    private final DependencyBuilder builder = new DependencyBuilder();

    /**
     * @param plugin Your plugin instance
     */
    public Lilliputian(@NotNull Loader plugin) {
        Lilliputian.plugin = plugin;
        path = plugin.getDataFolder() + File.separator + "LilliputianLibraries";
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
