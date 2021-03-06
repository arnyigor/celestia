package com.arny.celestiatools.controller;


import com.arny.celestiatools.models.CelestiaAsteroid;
import com.arny.celestiatools.utils.BaseUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

/**
 * Created by i.sedoy on 21.03.17.
 */
public class SqliteConnection {
    public static final String DB_TABLE_ASTEROIDS = "asteroids";
    public static final String DB_ASTER_KEY_ID = "id";
    public static final String DB_ASTER_KEY_ORBIT_TYPE = "orbit_type";
    public static final String DB_ASTER_KEY_NAME = "name";
    public static final String DB_ASTER_KEY_PERIOD = "period";
    public static final String DB_ASTER_KEY_RADIUS = "radius";
    public static final String DB_ASTER_KEY_SMA = "sma";
    public static final String DB_ASTER_KEY_INC = "inc";
    public static final String DB_ASTER_KEY_NODE = "node";
    public static final String DB_ASTER_KEY_ECC = "ecc";
    public static final String DB_ASTER_KEY_PERIC = "peric";
    public static final String DB_ASTER_KEY_MA = "ma";
    public static final String DB_ASTER_KEY_EPOCH = "epoch";
    public static final String DB_ASTER_UPDATE_TIME = "update_datetime";
    public static final String DB_ASTER_UPDATE_TIME_FORMAT = "dd MM yyyy HH:mm";
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS asteroids  ( id  INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , name  VARCHAR, orbit_type VARCHAR, radius REAL, period  REAL, sma REAL, inc REAL, node REAL, ecc REAL, peric REAL, ma REAL, epoch REAL,update_datetime TEXT)";
    private static Connection connection = null;

    public static Connection getConnection() {
        return connectDb();
    }

    public static void dbInit() {
        executeSQL(CREATE_TABLE);
    }

    @NotNull
    private static String getDbName() {
        String appDir = System.getProperty("user.dir");
        String workDir = "files";
        File file = new File(appDir + "/" + workDir);
        boolean exists = file.exists();
        if (!exists) {
            file.mkdirs();
        }
        return appDir + "/" + workDir + "/celestia.db";
    }

    private static Connection connectDb() {
        try {
            if (connection == null) {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:" + getDbName());
                return connection;
            } else {
                return connection;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static int executeSQLUpdate(String query) {
        Connection c = connectDb();
        int res = 0;
        try {
            Statement sta = c.createStatement();
            res = sta.executeUpdate(query);
            sta.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Nullable
    public static ResultSet executeSQLQuery(String query) {
        Connection c = connectDb();
        ResultSet resultSet = null;
        try {
            Statement sta = c.createStatement();
            resultSet = sta.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static boolean executeSQL(String query) {
        Connection c = connectDb();
        boolean res = false;
        try {
            Statement sta = c.createStatement();
            res = sta.execute(query);
            sta.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }


    public static boolean updateSqliteData(Connection connection, String table, HashMap<String, String> colVals, String cond) {
        StringBuilder values = new StringBuilder();
        int index = 0;
        for (Map.Entry<String, String> entry : colVals.entrySet()) {
            if (index != 0) {
                values.append(",");
            }
            values.append(entry.getKey()).append("=").append(entry.getValue());
            index++;
        }
        String formattedSql = String.format("UPDATE %s SET %s WHERE %s", table, values.toString(), cond);
        return executeSQLUpdate(formattedSql) == 1;
    }

    public static boolean insertSqliteData(String table, HashMap<String, String> colVals) {
        StringBuilder values = new StringBuilder();
        int index = 0;
        values.append("(");
        for (Map.Entry<String, String> keys : colVals.entrySet()) {
            if (index != 0) {
                values.append(",");
            }
            values.append(keys.getKey());
            index++;
        }
        values.append(") values(");
        index = 0;
        for (Map.Entry<String, String> vals : colVals.entrySet()) {
            if (index != 0) {
                values.append(",");
            }
            values.append(vals.getValue());
            index++;
        }
        values.append(")");
        return executeSQL(String.format("INSERT INTO %s%s", table, values.toString()));
    }

    public static CelestiaAsteroid getAsteroid(Connection connection, String cond) {
        try {
            CelestiaAsteroid asteroid = null;
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM " + DB_TABLE_ASTEROIDS + " WHERE " + cond;
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                asteroid = new CelestiaAsteroid();
                setAsteroidData(asteroid, resultSet);
            }
            return asteroid;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * set db values
     *
     * @param dbValues
     * @param asteroid
     */
    public static void setAsterDbValues(HashMap<String, String> dbValues, CelestiaAsteroid asteroid) {
        dbValues.put(SqliteConnection.DB_ASTER_KEY_NAME, "'" + asteroid.getName() + "'");
        dbValues.put(SqliteConnection.DB_ASTER_KEY_RADIUS, String.valueOf(asteroid.getRadius()));
        dbValues.put(SqliteConnection.DB_ASTER_KEY_ORBIT_TYPE, "'" + asteroid.getOrbitType() + "'");
        dbValues.put(SqliteConnection.DB_ASTER_KEY_PERIOD, String.valueOf(asteroid.getPeriod()));
        dbValues.put(SqliteConnection.DB_ASTER_KEY_SMA, String.valueOf(asteroid.getSma()));
        dbValues.put(SqliteConnection.DB_ASTER_KEY_INC, String.valueOf(asteroid.getInc()));
        dbValues.put(SqliteConnection.DB_ASTER_KEY_NODE, String.valueOf(asteroid.getNode()));
        dbValues.put(SqliteConnection.DB_ASTER_KEY_ECC, String.valueOf(asteroid.getEcc()));
        dbValues.put(SqliteConnection.DB_ASTER_KEY_PERIC, String.valueOf(asteroid.getPeric()));
        dbValues.put(SqliteConnection.DB_ASTER_KEY_MA, String.valueOf(asteroid.getMa()));
        dbValues.put(SqliteConnection.DB_ASTER_KEY_EPOCH, String.valueOf(asteroid.getEpoch()));
        dbValues.put(SqliteConnection.DB_ASTER_UPDATE_TIME, "'" + String.valueOf(BaseUtils.getDateTime(System.currentTimeMillis(), DB_ASTER_UPDATE_TIME_FORMAT)) + "'");
    }

    private static void setAsteroidData(CelestiaAsteroid asteroid, ResultSet resultSet) throws SQLException {
        asteroid.setName(resultSet.getString(DB_ASTER_KEY_NAME));
        asteroid.setRadius(resultSet.getDouble(DB_ASTER_KEY_RADIUS));
        asteroid.setOrbitType(resultSet.getString(DB_ASTER_KEY_ORBIT_TYPE));
        asteroid.setPeriod(resultSet.getDouble(DB_ASTER_KEY_PERIOD));
        asteroid.setSma(resultSet.getDouble(DB_ASTER_KEY_SMA));
        asteroid.setInc(resultSet.getDouble(DB_ASTER_KEY_INC));
        asteroid.setNode(resultSet.getDouble(DB_ASTER_KEY_NODE));
        asteroid.setEcc(resultSet.getDouble(DB_ASTER_KEY_ECC));
        asteroid.setPeric(resultSet.getDouble(DB_ASTER_KEY_PERIC));
        asteroid.setMa(resultSet.getDouble(DB_ASTER_KEY_MA));
        asteroid.setEpoch(resultSet.getDouble(DB_ASTER_KEY_EPOCH));
        asteroid.setUpdateTime(resultSet.getString(DB_ASTER_UPDATE_TIME));
    }

    public static ArrayList<CelestiaAsteroid> getAllCelestiaAsteroids() {
        try {
            ArrayList<CelestiaAsteroid> asteroids = new ArrayList<>();
            String sql = "SELECT * FROM " + DB_TABLE_ASTEROIDS;
            ResultSet resultSet = executeSQLQuery(sql);
            if (resultSet != null) {
                while (resultSet.next()) {
                    CelestiaAsteroid asteroid = new CelestiaAsteroid();
                    setAsteroidData(asteroid, resultSet);
                    asteroids.add(asteroid);
                }
            }
            return asteroids;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
