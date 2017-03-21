package com.arny.celestiatools.models;

import java.sql.*;
import javax.swing.*;

/**
 * Created by i.sedoy on 21.03.17.
 */
public class SqliteConnection {
    public static Connection dbConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + System.getProperty("user.dir") + "/sqlite/celestia.sqlite");
            System.out.println("url = " + connection.getMetaData().getURL());
            return connection;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
    }

}
