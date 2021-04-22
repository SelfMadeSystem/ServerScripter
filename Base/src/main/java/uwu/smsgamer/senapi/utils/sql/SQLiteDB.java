/*
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package uwu.smsgamer.senapi.utils.sql;

import java.sql.*;

/**
 * An Implementation of {@link SenDB} for SQLite.
 */
public class SQLiteDB extends AbstractSQLDB {
    public String path;

    public SQLiteDB(String path) {
        this.path = path;
    }

    @Override
    public void connect() {
        if (!isConnected()) {
            try {
                con = DriverManager.getConnection("jdbc:sqlite:" + path);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String tableName(String name) {
        return name;
    }

    @Override
    public Table newTable(String name) {
        return new SQLiteTable(this, name);
    }

    private static class SQLiteTable extends AbstractTable {
        private SQLiteTable(SQLiteDB db, String name) {
            super(db, name);
        }

        @Override
        public void truncate() {
            try {
                db.update("DELETE FROM " + getName() + ";");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
