/*
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package uwu.smsgamer.senapi.utils.spigot;

import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * An {@link OfflinePlayer} implementation that represents the console.
 */
public class SConsolePlayer implements OfflinePlayer {
    private static final String NAME = "CONSOLE";
    private static final UUID UUID = new UUID(0L, 0L);

    private static SConsolePlayer INSTANCE;

    public static SConsolePlayer getInstance() {
        if (INSTANCE == null) INSTANCE = new SConsolePlayer();
        return INSTANCE;
    }

    public static OfflinePlayer getOfflinePlayer(CommandSender sender) {
        if (sender instanceof OfflinePlayer) return (OfflinePlayer) sender;
        return getInstance();
    }

    public static OfflinePlayer deserialize(Map<String, Object> args) {
        return getInstance();
    }

    @Override
    public boolean isOnline() {
        return true;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public UUID getUniqueId() {
        return UUID;
    }

    @Override
    public boolean isBanned() {
        return false;
    }

    @Override
    public boolean isWhitelisted() {
        return true;
    }

    @Override
    public void setWhitelisted(boolean value) {
    }

    @Override
    public Player getPlayer() {
        return null;
    }

    @Override
    public long getFirstPlayed() {
        return 0;
    }

    @Override
    public long getLastPlayed() {
        return 0;
    }

    @Override
    public boolean hasPlayedBefore() {
        return true;
    }

    @Override
    public Location getBedSpawnLocation() {
        return null;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("UUID", getUniqueId().toString());
        return result;
    }

    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public void setOp(boolean value) {
    }
}
