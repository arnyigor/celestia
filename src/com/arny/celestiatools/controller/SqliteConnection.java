package com.arny.celestiatools.controller;


import com.arny.celestiatools.models.CelestiaAsteroid;
import com.arny.celestiatools.utils.BaseUtils;
import org.sqlite.util.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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

    public static Connection dbConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + System.getProperty("user.dir") + "/celestia.sqlite");
            Statement statement = connection.createStatement();
            statement.execute(CREATE_TABLE);
            System.out.println("url = " + connection.getMetaData().getURL());
            return connection;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
    }

    public static boolean updateSqliteData(Connection connection, String table, HashMap<String, String> colVals, String cond) {
        try {
            Statement statement = connection.createStatement();
            StringBuilder values = new StringBuilder();
            int index = 0;
            for (Map.Entry<String, String> entry : colVals.entrySet()) {
                if (index!=0){
                    values.append(",");
                }
                values.append(entry.getKey()).append("=").append(entry.getValue());
                index++;
            }
            String formattedSql = String.format("UPDATE %s SET %s WHERE %s",table,values.toString(),cond);
//            System.out.println("sql = " + formattedSql);
            return statement.executeUpdate(formattedSql)==1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean insertSqliteData(Connection connection, String table, HashMap<String, String> colVals){
        try {
            Statement statement = connection.createStatement();
            StringBuilder values = new StringBuilder();

            int index = 0;
            values.append("(");
            for (Map.Entry<String, String> keys : colVals.entrySet()) {
                if (index!=0){
                    values.append(",");
                }
                values.append(keys.getKey());
                index++;
            }
            values.append(") values(");
            index = 0;
            for (Map.Entry<String, String> vals : colVals.entrySet()) {
                if (index!=0){
                    values.append(",");
                }
                values.append(vals.getValue());
                index++;
            }
            values.append(")");
            String formattedSql = String.format("INSERT INTO %s%s",table,values.toString());
//            System.out.println("sql = " + formattedSql);
            return statement.execute(formattedSql);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static CelestiaAsteroid getAsteroid(Connection connection,String cond) {
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
     * @param dbValues
     * @param asteroid
     */
    public static void setAsterDbValues(HashMap<String,String> dbValues,CelestiaAsteroid asteroid) {
        dbValues.put(SqliteConnection.DB_ASTER_KEY_NAME, "'"+asteroid.getName()+"'");
        dbValues.put(SqliteConnection.DB_ASTER_KEY_RADIUS, String.valueOf(asteroid.getRadius()));
        dbValues.put(SqliteConnection.DB_ASTER_KEY_ORBIT_TYPE, "'"+asteroid.getOrbitType()+"'");
        dbValues.put(SqliteConnection.DB_ASTER_KEY_PERIOD, String.valueOf(asteroid.getPeriod()));
        dbValues.put(SqliteConnection.DB_ASTER_KEY_SMA, String.valueOf(asteroid.getSma()));
        dbValues.put(SqliteConnection.DB_ASTER_KEY_INC, String.valueOf(asteroid.getInc()));
        dbValues.put(SqliteConnection.DB_ASTER_KEY_NODE, String.valueOf(asteroid.getNode()));
        dbValues.put(SqliteConnection.DB_ASTER_KEY_ECC, String.valueOf(asteroid.getEcc()));
        dbValues.put(SqliteConnection.DB_ASTER_KEY_PERIC, String.valueOf(asteroid.getPeric()));
        dbValues.put(SqliteConnection.DB_ASTER_KEY_MA, String.valueOf(asteroid.getMa()));
        dbValues.put(SqliteConnection.DB_ASTER_KEY_EPOCH, String.valueOf(asteroid.getEpoch()));
        dbValues.put(SqliteConnection.DB_ASTER_UPDATE_TIME, "'"+String.valueOf(BaseUtils.getDateTime(System.currentTimeMillis(),DB_ASTER_UPDATE_TIME_FORMAT))+"'");
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

    public static ArrayList<CelestiaAsteroid> getAllCelestiaAsteroids(Connection connection) {
        try {
            ArrayList<CelestiaAsteroid> asteroids = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + DB_TABLE_ASTEROIDS);
            while (resultSet.next()) {
                CelestiaAsteroid asteroid = new CelestiaAsteroid();
// System.out.println("Номер в выборке #" + resultSet.getRow()  + "\t Номер в базе #" + resultSet.getInt(DB_ASTER_KEY_ID) + "\t" + resultSet.getString(DB_ASTER_KEY_NAME));
                setAsteroidData(asteroid, resultSet);
                asteroids.add(asteroid);
            }
            return asteroids;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
