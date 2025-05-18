package net.gib.storage.cloud.mySql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    private final String ipAddress, port, username, password, database;
    private final iDataBaseType type;
    private Connection connection;

    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    public MySQL(String ipAddress, String port, String username, String password, String database, iDataBaseType type) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
        this.type = type;
        updateConnection();
    }
    public iDataBaseType getType() {
        return type;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDatabase() {
        return database;
    }

    public String getUrl() {
        return "jdbc:" + type.getType() + "://" + ipAddress + ":" + port + "/" + database;
    }

    public Connection getConnection() {
        if (connection == null) {
            updateConnection();
        }
        return connection;
    }

    public void updateConnection() {
        try {
            this.connection = DriverManager.getConnection(getUrl(),this.username,this.password);
            System.out.println(ANSI_GREEN + "Connected to database: " + database + ANSI_RESET);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public interface iDataBaseType {
        String getType();
    }
    public enum DataBaseType implements iDataBaseType{
        MYSQL("mysql"),
        MARIADB("mariadb");

        private final String type;
        DataBaseType(String type) {
            this.type = type;
        }

        @Override
        public String getType() {
            return type;
        }
    }

}
