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
            return DriverManager.getConnection("jdbc:sqlite:" + System.getProperty("user.dir") + "\\sqlite\\celestia.sqlite");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
    }

}
