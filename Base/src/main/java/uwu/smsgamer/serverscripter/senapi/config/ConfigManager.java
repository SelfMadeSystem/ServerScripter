/*
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package uwu.smsgamer.serverscripter.senapi.config;

import de.leonhard.storage.*;
import de.leonhard.storage.internal.settings.ReloadSettings;
import uwu.smsgamer.serverscripter.senapi.Loader;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;

/**
 * Abstract class to store configuration files.
 *
 * @author Sms_Gamer_3808 (Shoghi Simon)
 */
public class ConfigManager {
    protected static Loader pl;
    private static ConfigManager instance;
    public final Map<String, Config> configs = new HashMap<>();
    public final Set<ConfVal<?>> vals = new HashSet<>();

    public static ConfigManager getInstance() {
        if (instance == null) instance = new ConfigManager();
        return instance;
    }

    public static void setInstance(ConfigManager instance) {
        ConfigManager.instance = instance;
    }

    private static void saveResource(String resourcePath, File dataFolder) {
        if (new Throwable().getStackTrace()[0].getClassName().startsWith("org.python"))
            throw new IllegalStateException("Don't u dare execute this from Python!");
        if (resourcePath == null || resourcePath.equals("")) {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }

        resourcePath = resourcePath.replace('\\', '/');
        InputStream in = getResource(resourcePath);
        if (in == null)
            return;
//            throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in " + pl.getFile());

        File outFile = new File(dataFolder, resourcePath);
        int lastIndex = resourcePath.lastIndexOf('/');
        File outDir = new File(dataFolder, resourcePath.substring(0, Math.max(lastIndex, 0)));

        if (!outDir.exists()) {
            if (!outDir.mkdirs()) {
                throw new RuntimeException("The directory '" + outDir.getAbsolutePath() + "' could not be created");
            }
        }

        try {
            if (!outFile.exists()) {
                OutputStream out = new FileOutputStream(outFile);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            } else {
                pl.getLogger().log(Level.WARNING, "Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
            }
        } catch (IOException ex) {
            pl.getLogger().log(Level.SEVERE, "Could not save " + outFile.getName() + " to " + outFile, ex);
        }
    }

    public static InputStream getResource(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("Filename cannot be null");
        }

        try {
            URL url = pl.
                    getClass().
                    getClassLoader().
                    getResource(filename);

            if (url == null) {
                return null;
            }

            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection.getInputStream();
        } catch (IOException ex) {
            return null;
        }
    }

    private void checkDataFolder() {
        if (!pl.getDataFolder().exists()) {
            if (!pl.getDataFolder().mkdirs()) {
                throw new RuntimeException("The data folder could not be created");
            }
        }
    }

    public void setup(String... configs) {
        checkDataFolder();
        for (String config : configs) {
            pl.getLogger().info("Loading config: " + config);
            try {
                loadConfig(config);
                saveConfig(config);
                pl.getLogger().info("Loaded config: " + config);
            } catch (Exception e) {
                e.printStackTrace();
                pl.getLogger().severe("Error while loading config: " + config);
            }
        }
    }

    public File configFile(String name) {
        return new File(pl.getDataFolder(), name + ".yml");
    }

    public void reloadConfig(String name) {
        getConfig(name).forceReload();
    }

    private void loadConfig(String name) {
        if (new Throwable().getStackTrace()[0].getClassName().startsWith("org.python"))
            throw new IllegalStateException("Don't u dare execute this from Python!");

        File configFile = configFile(name);
        if (!configFile.exists())
            saveResource(name + ".yml", pl.getDataFolder());
        loadConfig(name, configFile);
    }

    public void loadConfig(String name, File file) {
        checkDataFolder();
        Config config = LightningBuilder.fromFile(file).createConfig();
        config.setReloadSettings(ReloadSettings.MANUALLY);
        configs.remove(config.getName());
        configs.put(name, config);
    }

    public void saveConfig(String name) {
        try {
            Config config = getConfig(name);
            config.write();
        } catch (Exception e) {
            pl.getLogger().warning("Exception thrown when saving config: " + name);
            pl.getLogger().warning("If it's a StringIndexOutOfBoundsException, please ignore it. For some reason it happens and I have no idea why but nothing seems to be broken so idc.");
            e.printStackTrace();
        }
    }

    public void reloadAllConfigs() {
        for (Map.Entry<String, Config> entry : getConfigs()) {
            entry.getValue().forceReload();
        }
        for (ConfVal<?> val : vals) {
            reloadConfVal(val);
        }
    }

    public Config getConfig(String name) {
        return configs.get(name);
    }

    public Set<Map.Entry<String, Config>> getConfigs() {
        return configs.entrySet();
    }

    public <T> void reloadConfVal(ConfVal<T> val) {
        setConfVal(val, val.dVal);
    }

    @SuppressWarnings("unchecked")
    public <T> void setConfVal(ConfVal<T> val, T dVal) {
        if (dVal instanceof Map) {
            vals.add(val);
            Config config = configs.get(val.config);
            if (!config.contains(val.name)) {
                config.set(val.name, dVal);
                val.value = dVal;
            } else {
                HashMap<String, Object> map = new HashMap<>();
                for (String key : config.getSection(val.name).singleLayerKeySet())
                    map.put(key, config.get(val.name + "." + key));
                val.value = (T) map;
            }
        } else {
            vals.add(val);
            Config config = configs.get(val.config);
            val.value = config.get(val.name, dVal);
            if (!config.contains(val.name)) config.set(val.name, dVal);
        }
    }
}
