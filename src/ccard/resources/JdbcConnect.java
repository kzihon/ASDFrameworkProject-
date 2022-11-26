package ccard.resources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnect {
    Connection conn = null;
    private String userName;
    private String password;
    private String host;
    private String  port;

    private String dbName;

    private String driverName;

    public JdbcConnect(String driverName, String userName, String password, String host, String port, String dbName ) throws SQLException {
        this.driverName = driverName;
        this.userName= userName;
        this.password= password;
        this.host =host;
        this.port = port;
        this.dbName= dbName;
    }

    public Connection setConnection() {
        try {
            Class.forName(getDriverName());
            conn = DriverManager.getConnection("jdbc:mysql://" + getHost() + ":"
                    + getPort() + "/" + getDbName() + "?" +
                    "user=" + getUserName() + "&password=" + getPassword());
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return conn;
    }

    public  Connection initJdbc() throws SQLException {
        return this.setConnection();
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}
