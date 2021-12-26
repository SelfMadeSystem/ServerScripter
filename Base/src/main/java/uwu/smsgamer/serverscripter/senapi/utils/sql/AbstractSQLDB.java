/*
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package uwu.smsgamer.serverscripter.senapi.utils.sql;

import uwu.smsgamer.serverscripter.senapi.utils.Pair;

import java.sql.*;
import java.util.*;

/**
 * An abstract implementation of {@link SenDB} for SQL.
 */
public abstract class AbstractSQLDB implements SenDB {
    public Connection con;

    @Override
    public void initialize(Pair<String, String>... rows) {
        connect();
        for (Pair<String, String> row : rows) {
            createTable(row.a, row.b);
        }
    }

    @Override
    public Connection getConnection() {
        return con;
    }

    @Override
    public Table getTable(String name) {
        if (!tableExists(name)) System.err.println("Table doesn't exist. Might cause errors!");
        return newTable(name);
    }

    @Override
    public Table createTable(String name, String columns) {
        try {
            PreparedStatement ps = getConnection().prepareStatement(
                    "CREATE TABLE IF NOT EXISTS " + tableName(name) + " (" + columns + ");");
            ps.executeUpdate();
            return newTable(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean tableExists(String name) {
        try { //100% skidded from vagdedes but optimized a bit.
            Connection connection = getConnection();
            if (connection == null) return false;

            DatabaseMetaData metadata = connection.getMetaData();
            if (metadata == null) return false;

            ResultSet rs = metadata.getTables(null, null, tableName(name), null);
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public abstract String tableName(String name);

    public abstract Table newTable(String name);

    protected abstract static class AbstractTable implements Table {
        protected final AbstractSQLDB db;
        protected final String name;

        protected AbstractTable(AbstractSQLDB database, String name) {
            this.db = database;
            this.name = name;
        }

        @Override
        public String getName() {
            return db.tableName(name);
        }

        @Override
        public void delete() {
            try {
                db.update("DROP TABLE " + getName() + ";");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void truncate() {
            try {
                db.update("TRUNCATE TABLE " + getName() + ";");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int countRows() {
            try {
                ResultSet rs = db.query("SELECT * FROM " + getName());
                int i = 0;
                while (rs.next()) i++;
                return i;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        public boolean exists(String condition, Object... params) {
            try {
                ResultSet rs = db.query("SELECT * FROM " + getName() + " WHERE " + condition + ";", params);
                return rs.next();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return false;
        }

        @Override
        public void add(String columns, String values, Object... objects) {
            try {
                db.update("INSERT INTO " + getName() + " (" + columns + ") VALUES (" + values + ");", objects);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void removeFromTable(String condition, Object... params) {
            try {
                db.update("DELETE FROM " + getName() + " WHERE " + condition + ";", params);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

//        @Override // Don't use upsert
//        public void upsert(String selected, Object object, String condition, Object... params) {
//            try {
//                ResultSet rs = db.query("SELECT * FROM " + getName() + " WHERE " + condition + ";", params);
//                if (rs.next()) {
//                    Object[] updateParams = new Object[params.length + 1];
//                    System.arraycopy(params, 0, updateParams, 1, params.length);
//                    db.update("UPDATE " + getName() + " SET " + selected + "=? WHERE " + condition + ";", updateParams);
//                } else {
//                    add(column + ", " + selected, "'" + checkData + "', '" + object + "'");
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }

        @Override
        public void set(String selected, Object object, String condition, Object... params) {
            try {
                Object[] updateParams = new Object[params.length + 1];
                System.arraycopy(params, 0, updateParams, 1, params.length);
                updateParams[0] = object;
                db.update("UPDATE " + getName() + " SET " + selected + "=? WHERE " + condition + ";", updateParams);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public Object get(String selected, String condition, Object... params) {
            try {
                ResultSet rs = db.query("SELECT * FROM " + getName() + " WHERE " + condition + ";", params);
                if (rs.next()) return rs.getObject(selected);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public Map<String, Object> get(String condition, Object... params) {
            try {
                ResultSet rs = db.query("SELECT * FROM " + getName() + " WHERE " + condition + ";", params);
                ResultSetMetaData meta = rs.getMetaData();
                int cCount = meta.getColumnCount();
                Map<String, Object> map = new LinkedHashMap<>();
                if (rs.next()) {
                    for (int i = 1; i <= cCount; i++) {
                        map.put(meta.getColumnName(i), rs.getObject(i));
                    }
                    return map;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public List<?> getList(String selected, String condition, Object... params) {
            try {
                ResultSet rs = db.query("SELECT * FROM " + getName() + " WHERE " + condition + ";", params);
                List<Object> list = new ArrayList<>();
                while (rs.next()) list.add(rs.getObject(selected));
                return list;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public List<?> getAll(String selected) {
            try {
                ResultSet rs = db.query("SELECT * FROM " + getName() + ";");
                List<Object> list = new ArrayList<>();
                while (rs.next()) list.add(rs.getObject(selected));
                return list;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> Map<String, List<T>> getAll() {
            try {
                ResultSet rs = db.query("SELECT * FROM " + getName() + ";");
                ResultSetMetaData meta = rs.getMetaData();
                int cCount = meta.getColumnCount();
                Map<String, List<T>> map = new LinkedHashMap<>();
                while (rs.next()) {
                    for (int i = 1; i <= cCount; i++) {
                        String name = meta.getColumnName(i);
                        if (map.containsKey(name)) map.get(name).add((T) rs.getObject(i));
                        else map.put(name, new ArrayList<>(Collections.singleton((T) rs.getObject(i))));
                    }
                }
                return map;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
