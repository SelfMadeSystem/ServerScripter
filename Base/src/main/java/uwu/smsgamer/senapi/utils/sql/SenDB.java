/*
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package uwu.smsgamer.senapi.utils.sql;

import uwu.smsgamer.senapi.utils.Pair;

import java.sql.*;
import java.util.*;

/**
 * A database interface. Thank you Vagdedes a *lot* for helping me figure out the methods I wanted to use.
 */
public interface SenDB {
    void initialize(Pair<String, String>... rows);

    /**
     * Connects to the database.
     */
    void connect();

    /**
     * Disconnects from the database.
     */
    default void disconnect() {
        if (!isConnected()) {
            try {
                getConnection().close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /**
     * Should be called before every action. If we are no longer connected to the database, then we connect again.
     */
    default void testConnection() {
        if (!isConnected()) connect();
    }

    /**
     * Gets the connection to the database.
     *
     * @return The connection to the database.
     */
    Connection getConnection();

    /**
     * Gets if we are connected to the database.
     *
     * @return If we are connected to the database.
     */
    default boolean isConnected() {
        try {
            return getConnection() != null && !getConnection().isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gets a table.
     *
     * @param name The name of the table.
     * @return The table.
     */
    Table getTable(String name);

    /**
     * Creates a new table.
     *
     * @param name    The name of the table.
     * @param columns The columns the table should have.
     * @return The table that you have created.
     */
    Table createTable(String name, String columns);

    /**
     * Returns if a table exists.
     *
     * @param name The name of the table.
     * @return if a table exists.
     */
    boolean tableExists(String name);

    /**
     * Runs an update statement.
     *
     * @param update The command to do.
     * @throws SQLException if an SQL error occurred.
     */
    default void update(String update, Object... objects) throws SQLException {
        PreparedStatement ps = getConnection().prepareStatement(update);
        for (int i = 0; i < objects.length; i++) ps.setObject(i + 1, objects[i]);
        ps.executeUpdate();
    }

    /**
     * Runs an query statement.
     *
     * @param query The command to do.
     * @return The result of the query.
     * @throws SQLException if an SQL error occurred.
     */
    default ResultSet query(String query, Object... objects) throws SQLException {
        PreparedStatement ps = getConnection().prepareStatement(query);
        for (int i = 0; i < objects.length; i++) ps.setObject(i + 1, objects[i]);
        return ps.executeQuery();
    }

    /**
     * A table interface that all tables should implement.
     * Please, someone, tell me how tf types work bc OMG IT'S SHITTY IN JAVA
     */
    interface Table {
        /**
         * For internal use. Returns the array joined by {@code " AND "}.
         *
         * @param where The array to use.
         * @return The array joined by {@code " AND "}.
         */
        static String getCondition(String... where) {
            return String.join(" AND ", where);
        }

        /**
         * Gets the name of the table.
         *
         * @return The name of the table.
         */
        String getName();

        /**
         * Deletes the table from the database.
         */
        void delete();

        /**
         * Truncates/clears the table. Removes all elements from the table.
         */
        void truncate(); // Clears

        /**
         * Counts the number of rows in the table.
         *
         * @return The number of rows in the table.
         */
        int countRows();

        /**
         * Checks if a row with said params exists.
         *
         * @param condition The condition of the objects.
         * @param params    The parameters (replaces questions marks `?`)
         * @return If a value in a column exists.
         */
        boolean exists(String condition, Object... params);

        /**
         * Adds a row to the table.
         *
         * @param columns The columns of the table.
         * @param values  The values of the row to add.
         */
        void add(String columns, String values, Object... objects);

//        /**
//         * Don't use Upsert.
//         *
//         * @param selected a
//         * @param object a
//         * @param condition The condition of the objects.
//         * @param params The parameters (replaces questions marks `?`)
//         */
//        void upsert(String selected, Object object, String condition, Object... params);

        /**
         * Removes a row from the table.
         *
         * @param condition The condition of the objects.
         * @param params    The parameters (replaces questions marks `?`)
         */
        void removeFromTable(String condition, Object... params);

        /**
         * Sets a value in the table.
         *
         * @param selected  The select column to set.
         * @param object    The object to set it to.
         * @param condition The condition of the objects.
         * @param params    The parameters (replaces questions marks `?`)
         */
        void set(String selected, Object object, String condition, Object... params);

        /**
         * Gets a value from the table.
         *
         * @param selected  The selected column.
         * @param condition The condition of the objects.
         * @param params    The parameters (replaces questions marks `?`)
         * @return A value from the table.
         */
        Object get(String selected, String condition, Object... params);

        /**
         * Gets a row from the table.
         *
         * @param condition The condition of the objects.
         * @param params    The parameters (replaces questions marks `?`)
         * @return A value from the table.
         */
        Map<String, Object> get(String condition, Object... params);

        /**
         * Gets all the values from the table that satisfies a condition.
         *
         * @param selected  The selected column.
         * @param condition The condition of the objects.
         * @param params    The parameters (replaces questions marks `?`)
         * @return All the values from the table that satisfies a condition.
         */
        List<?> getList(String selected, String condition, Object... params);

        /**
         * Gets all the values from a row in the table.
         *
         * @param selected The selected column.
         * @return All the values from a row in the table.
         */
        List<?> getAll(String selected);

        /**
         * Gets all the values from all the rows in the table.
         *
         * @return All the values from all the rows in the table.
         */
        <T> Map<String, List<T>> getAll();
    }
}
